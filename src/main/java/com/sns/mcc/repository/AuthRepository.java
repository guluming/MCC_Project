package com.sns.mcc.repository;

import com.sns.mcc.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByRefreshToken(String refreshToken);
}
