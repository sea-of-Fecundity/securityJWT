package com.example.securityjwt.repository;

import com.example.securityjwt.domain.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByRefresh(String refresh);

    Boolean existsByUserAddress(String address);
    @Transactional
    void deleteByRefresh(String refresh);
}
