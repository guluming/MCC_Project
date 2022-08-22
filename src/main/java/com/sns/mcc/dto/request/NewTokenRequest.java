package com.sns.mcc.dto.request;

import lombok.Getter;

@Getter
public class NewTokenRequest {
    private String accesstoken;
    private String refreshtoken;
}
