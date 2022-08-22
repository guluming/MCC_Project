package com.sns.mcc.dto.response;

import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private int code;
    private String message;
    private HttpStatus errorStstus;
}
