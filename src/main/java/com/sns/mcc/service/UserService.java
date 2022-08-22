package com.sns.mcc.service;

import com.sns.mcc.dto.request.*;
import com.sns.mcc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    //회원가입
    @Transactional
    public ResponseEntity<?> userSignup(UserSignUpRequest userSignUpRequest) {

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //아이디 중복 확인
    public ResponseEntity<?> duplicateCheckUsername(DuplicateCheckUsernameRequest duplicateCheckUsernameRequest) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //닉네임 중복 확인
    public ResponseEntity<?> duplicateCheckNickname(DuplicateCheckNicknameRequest duplicateCheckNicknameRequest) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //로그인
    public ResponseEntity<?> userLogin(UserLoginRequest userLoginRequest) {

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //회원정보 수정
    @Transactional
    public ResponseEntity<?> userEdit(UserEditRequest userEditRequest) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
