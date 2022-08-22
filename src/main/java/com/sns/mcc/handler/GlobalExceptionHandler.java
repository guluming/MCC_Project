package com.sns.mcc.handler;

import com.sns.mcc.dto.response.ErrorResponse;
import com.sns.mcc.enums.ErrorCode;
import com.sns.mcc.exception.CustomSignUpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomSignUpException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleCustomSignUpException(CustomSignUpException ex){
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.SIGN_UP_DATA_VALID.getCode(), ErrorCode.SIGN_UP_DATA_VALID.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
