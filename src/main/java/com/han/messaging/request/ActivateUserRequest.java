package com.han.messaging.request;

import lombok.Data;

@Data
public class ActivateUserRequest {
    private String identification; // username/email
    private String validationCode;
    private String password;
}
