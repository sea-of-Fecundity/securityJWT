package com.example.securityjwt.controller;


import com.example.securityjwt.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@RestControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> jwtException(Exception e) {
        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(SC_UNAUTHORIZED))
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(SC_UNAUTHORIZED)
                .body(body);
    }
}
