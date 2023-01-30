package com.han.messaging.response;

import com.han.messaging.enums.StatusCode;

public class UserLoginResponse extends CommonResponse {
    String loginToken;

    public UserLoginResponse(String loginToken) {
        super(StatusCode.OK);
        this.loginToken = loginToken;
    }

    public String getLoginToken() {
        return loginToken;
    }
}
