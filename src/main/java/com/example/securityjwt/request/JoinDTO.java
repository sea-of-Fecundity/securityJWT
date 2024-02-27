package com.example.securityjwt.request;


import com.example.securityjwt.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class JoinDTO {

    @NotBlank
    @Email
    private final String address;

    @NotBlank
    private final String userName;

    @NotBlank
    private final String password;

    @Builder
    public JoinDTO(@JsonProperty("address") String address, @JsonProperty("userName")String userName,@JsonProperty("password")String password) {
        this.address = address;
        this.userName = userName;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .address(this.getAddress())
                .userName(this.getUserName())
                .password(this.password)
                .build();
    }
}