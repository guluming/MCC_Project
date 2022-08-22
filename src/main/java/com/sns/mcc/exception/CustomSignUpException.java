package com.sns.mcc.exception;

import com.sns.mcc.enums.ErrorCode;

public class CustomSignUpException extends IllegalArgumentException {
    private final ErrorCode errorType;

    public CustomSignUpException(ErrorCode errorType) {
        this.errorType = errorType;
    }
}
