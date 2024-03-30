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


}
