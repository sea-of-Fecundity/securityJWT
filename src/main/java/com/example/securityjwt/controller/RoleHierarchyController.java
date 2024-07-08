package com.example.securityjwt.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleHierarchyController {

    @GetMapping("/visitor")
    public String visitor() {
        return "hello visitor!";
    }

    @GetMapping("/test")
    public String test() {
        return "hello test!";
    }
}
