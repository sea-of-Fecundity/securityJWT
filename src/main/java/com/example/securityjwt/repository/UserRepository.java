package com.example.securityjwt.repository;

import com.example.securityjwt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public boolean existsByAddress(String address);
}
