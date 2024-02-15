package com.example.securityjwt.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

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
    public User(String address, String userName, String password) {
        this.address = address;
        this.userName = userName;
        this.password = password;
    }

}