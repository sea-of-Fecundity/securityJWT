package com.example.securityjwt.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.FieldError;

@Data
public class ErrorDTO {

    private final String field;
    private final String errorMessage;


    @Builder
    public ErrorDTO(String field, String errorMessage) {
        this.field = field;
        this.errorMessage = errorMessage;
    }

    public ErrorDTO(FieldError fieldError) {
        this.field = fieldError.getField();
        this.errorMessage = fieldError.getDefaultMessage();
    }
}
