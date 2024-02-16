package com.example.securityjwt.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String address;

    private String userName;

    private String password;

    private String role;

    public void passwordEncode(String password) {
        this.password = password;
    }

    @Builder
    public Member(String address, String userName, String password, String role) {
        this.address = address;
        this.userName = userName;
        this.password = password;
        this.role = role != null ? role : "ROLE_USER";

    }

}