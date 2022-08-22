package com.sns.mcc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sns.mcc.dto.request.*;
import com.sns.mcc.dto.response.UserDuplicateResponse;
import com.sns.mcc.dto.response.UserLoginResponse;
import com.sns.mcc.security.UserDetailsImpl;
import com.sns.mcc.service.KakaoUserService;
import com.sns.mcc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    //회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<Object> userSignup(@RequestBody UserSignUpRequest userSignUpRequest) {
        return userService.userSignup(userSignUpRequest);
    }

    //아이디 중복 확인
    @PostMapping("/user/id")
    public UserDuplicateResponse duplicateCheckUsername(@RequestBody DuplicateCheckUsernameRequest duplicateCheckUsernameRequest) {
        return userService.duplicateCheckUsername(duplicateCheckUsernameRequest.getUsername());
    }

    //닉네임 중복 확인
    @PostMapping("/user/nickname")
    public UserDuplicateResponse duplicateCheckNickname(@RequestBody DuplicateCheckNicknameRequest duplicateCheckNicknameRequest) {
        return userService.duplicateCheckNickname(duplicateCheckNicknameRequest.getNickname());
    }

    //로그인
    @PostMapping("/user/login")
    public ResponseEntity<Object> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        return userService.userLogin(userLoginRequest);
    }

    //소셜 로그인
    @PostMapping("/user/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        return kakaoUserService.kakaoLogin(code);
    }

    //Access 토큰 재발급
    @PostMapping("/home/token")
    public ResponseEntity<?> newAccessToken(@RequestBody NewTokenRequest newTokenRequest){
        return userService.newAccessToken(newTokenRequest);
    }

    //회원정보 수정
    @PatchMapping("/home/user")
    public ResponseEntity<Object> userEdit(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody UserEditRequest userEditRequest) {
        return userService.userEdit(userDetails, userEditRequest);
    }
}
