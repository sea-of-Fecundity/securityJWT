package com.example.securityjwt.service;


import com.example.securityjwt.domain.User;
import com.example.securityjwt.exception.user.DuplicateAddress;
import com.example.securityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void save(User user) {

        if (userRepository.existsByAddress(user.getAddress())) {
            throw new DuplicateAddress("중복된 이메일입니다.");
        }
        else {
            String encoded = passwordEncoder.encode(user.getPassword());
            user.passwordEncode(encoded);
            userRepository.save(user);
        }

    }

}
