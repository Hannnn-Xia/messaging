package com.han.messaging.request;

import lombok.Data;

import java.sql.Date;

@Data
public class AddFriendRequest {
    private String loginToken;
    private String message;
    private String identification; // username, email
}
