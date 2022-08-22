package com.sns.mcc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sns.mcc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;

    public ResponseEntity<?> kakaoLogin(String code) throws JsonProcessingException {

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
