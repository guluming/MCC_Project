package com.sns.mcc.dto.request;

import lombok.Getter;

@Getter
public class UserSignUpRequest {
    private String username;
    private String password;
    private String passwordChk;
}
