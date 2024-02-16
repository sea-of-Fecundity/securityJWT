package com.example.securityjwt.repository;

import com.example.securityjwt.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {

    boolean existsByAddress(String address);

    Optional<Member> findByAddress(String address);

}
