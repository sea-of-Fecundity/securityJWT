package com.example.securityjwt.controller;

import com.example.securityjwt.request.JoinDTO;
import com.example.securityjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public void join(@Validated @RequestBody JoinDTO joinDTO) {
        userService.save(joinDTO.toEntity());
    }
}