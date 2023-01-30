package com.han.messaging.request;

import com.han.messaging.enums.Gender;
import lombok.Data;

@Data
public class RegisterUserRequest {
    private String username;
    private String password;
    private String repeatPassword;
    private String email;
    private String address;
    private Gender gender;
    private String nickname;
}
