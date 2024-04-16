package com.example.securityjwt.controller;


import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VisitorController {

    @GetMapping("/visitor")
    public String visitor() {
        return "hello visitor!";
    }
}
