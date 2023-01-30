package com.han.messaging.response;

import com.han.messaging.enums.StatusCode;
import com.han.messaging.model.User;

import java.util.List;

public class SearchUserResponse extends CommonResponse {
    List<User> users;

    public SearchUserResponse(List<User> users) {
        super(StatusCode.OK);
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
