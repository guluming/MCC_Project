package com.sns.mcc.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginResponse {
    private String accessToken;
    private String refreshToken;
    private boolean first = false;
}
