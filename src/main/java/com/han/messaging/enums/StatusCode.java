package com.han.messaging.enums;

import org.springframework.http.HttpStatus;

public enum StatusCode {

    OK(1000, "Successful", HttpStatus.OK),
    PASSWORDS_NOT_MATCHED(1001, "Passwords are not matched", HttpStatus.BAD_REQUEST),
    USERNAME_OR_EMAIL_IS_EMPTY(1002, "Username or email is empty", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT(1003, "Password is too short", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST(1004, "User doesn't exist", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1005, "Wrong password", HttpStatus.FORBIDDEN),
    WRONG_VALIDATION_CODE(1006, "Wrong Validation Code", HttpStatus.BAD_REQUEST),
    INACTIVE_USER(1007, "Inactive user.", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS_ALREADY(1008, "Email exists already", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS_ALREADY(1009, "Username exists already", HttpStatus.BAD_REQUEST),
    EXPIRED_LOGIN_TOKEN(1010, "Expired login token", HttpStatus.FORBIDDEN),

    INVITATION_NOT_EXIST(1011, "Invitation not exist", HttpStatus.BAD_REQUEST),

    ALREADY_FRIEND(1012, "Sender and Receiver are friends already", HttpStatus.FORBIDDEN),
    INVITATION_PENDING(1013, "Previous Invitation is Pending", HttpStatus.FORBIDDEN),
    INVITATION_REJECTED(1014, "Forbidden to Send Within Seven Days", HttpStatus.FORBIDDEN);

    private String message;
    private int code;
    private HttpStatus httpStatus;

    StatusCode(int code, String message, HttpStatus httpStatus) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
