package com.han.messaging.response;

import com.han.messaging.enums.FriendInvitationStatus;
import com.han.messaging.model.FriendInvitation;
import com.han.messaging.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class FriendInvitationResponse {
    private UserResponse sender;
    private Date sendTime;
    private String message;
    private FriendInvitationStatus status;
    private int friendInvitationId;

    public FriendInvitationResponse(FriendInvitation friendInvitation,
                                    User sender) {
        this.sender = new UserResponse(sender);
        this.sendTime = friendInvitation.getSendTime();
        this.message = friendInvitation.getMessage();
        this.status = friendInvitation.getStatus();
        this.friendInvitationId = friendInvitation.getId();
    }

}
