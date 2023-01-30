package com.han.messaging.response;

import com.han.messaging.enums.StatusCode;

import java.util.List;

public class ListFriendsResponse extends CommonResponse{
    List<UserResponse> friends;

    public ListFriendsResponse(List<UserResponse> friends) {
        super(StatusCode.OK);
        this.friends = friends;
    }

    public List<UserResponse> getFriends() {
        return friends;
    }
}
