package com.sns.mcc.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDuplicateResponse {
    private boolean exist;
    private String message;
}
