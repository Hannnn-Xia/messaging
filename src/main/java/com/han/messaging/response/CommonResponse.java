package com.han.messaging.response;

import com.han.messaging.enums.StatusCode;

public class CommonResponse {

    private String message;
    private int code;

    public CommonResponse(StatusCode statusCode) {
        this.message = statusCode.getMessage();
        this.code = statusCode.getCode();
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
