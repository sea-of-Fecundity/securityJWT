package com.example.securityjwt.request;


import com.example.securityjwt.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JoinDTO {

    @NotBlank
    @Email
    private final String address;

    @NotBlank
    private final String userName;

    @NotBlank
    private final String password;
    
    public User toEntity() {
        return User.builder()
                .address(this.getAddress())
                .userName(this.getUserName())
                .password(this.password)
                .build();
    }
}
