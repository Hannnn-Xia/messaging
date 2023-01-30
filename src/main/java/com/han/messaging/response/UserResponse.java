package com.han.messaging.response;

import com.han.messaging.enums.Gender;
import com.han.messaging.model.User;
import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private Gender gender;
    private String nickname;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.gender = user.getGender();
        this.nickname = user.getNickname();
    }
}
