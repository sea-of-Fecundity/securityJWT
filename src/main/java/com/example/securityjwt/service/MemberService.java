package com.example.securityjwt.service;


import com.example.securityjwt.domain.Member;
import com.example.securityjwt.exception.user.DuplicateAddress;
import com.example.securityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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