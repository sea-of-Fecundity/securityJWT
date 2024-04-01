package com.example.securityjwt.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refresh {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userAddress;

    private String refresh;

    private Long expired;

    @Builder
    public Refresh(String userAddress, String refresh, Long expired) {
        this.userAddress = userAddress;
        this.refresh = refresh;
        this.expired = expired;
    }
}
