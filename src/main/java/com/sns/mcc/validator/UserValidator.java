package com.sns.mcc.validator;

import com.sns.mcc.enums.ErrorCode;
import com.sns.mcc.exception.CustomSignUpException;

public class UserValidator {
    public static void validateUsername(String username){
        try {
            //ID 검사
            if(username == null){
                throw new IllegalArgumentException("아이디를 입력해 주세요");
            }
            if (username.trim().equals("")) {
                throw new IllegalArgumentException("아이디를 입력해 주세요");
            }
//            if (!username.matches("^[가-힣a-zA-Z]+$")) {
//                throw new IllegalArgumentException("아이디 형식을 확인해 주세요");
//            }
        } catch (Exception e) {
            throw new CustomSignUpException(ErrorCode.SIGN_UP_DATA_VALID);
        }
    }

    public static void validateNickname(String nickname){
        try {
            //닉네임 검사
            if(nickname == null){
                throw new IllegalArgumentException("닉네임을 입력해 주세요");
            }
            if (nickname.trim().equals("")) {
                throw new IllegalArgumentException("닉네임을 입력해 주세요");
            }
//            if (!nickname.matches("^[가-힣a-zA-Z]+$")) {
//                throw new IllegalArgumentException("닉네임 형식을 확인해 주세요");
//            }
        } catch (Exception e) {
            throw new CustomSignUpException(ErrorCode.SIGN_UP_DATA_VALID);
        }
    }

    public static void validatePassword(String password){
        try {
            //비밀번호 검사
            if(password == null){
                throw new IllegalArgumentException("비밀번호를 입력해 주세요");
            }
            if (password.trim().equals("")) {
                throw new IllegalArgumentException("비밀번호를 입력해 주세요");
            }
//            if (!password.matches("^[가-힣a-zA-Z]+$")) {
//                throw new IllegalArgumentException("비밀번호 형식을 확인해 주세요");
//            }
        } catch (Exception e) {
            throw new CustomSignUpException(ErrorCode.SIGN_UP_DATA_VALID);
        }
    }
}
