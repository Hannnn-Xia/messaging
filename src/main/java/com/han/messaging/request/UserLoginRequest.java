package com.han.messaging.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String identification;
    private String password;
}
