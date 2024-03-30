package com.example.securityjwt.service;


import com.example.securityjwt.domain.Member;
import com.example.securityjwt.exception.token.RefreshTokenNotFoundException;
import com.example.securityjwt.exception.user.DuplicateAddress;
import com.example.securityjwt.jwt.JwtUtil;
import com.example.securityjwt.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void save(Member member) {

        if (userRepository.existsByAddress(member.getAddress())) {
            throw new DuplicateAddress();
        }
        else {
            String encoded = passwordEncoder.encode(member.getPassword());
            member.passwordEncode(encoded);
            userRepository.save(member);
        }
    }


    public ResponseEntity<String> checkCookie(HttpServletRequest request, HttpServletResponse response) {
        String refresh = new String();

        Cookie[] cookies = request.getCookies();

        Cookie cookie = Arrays.stream(cookies)
                .filter(c -> "refresh".equals(c.getName())).findFirst().orElseThrow(RefreshTokenNotFoundException::new);


        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String address = jwtUtil.getAddress(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", address, role, 600000L);
        String newRefresh = jwtUtil.createJwt("access", address, role, 86400000L);
        response.setHeader("access", newAccess);
        response.addCookie(createCookis("refresh", newRefresh));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookis(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        return cookie;
    }

}
