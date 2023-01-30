package com.han.messaging.model;


import com.han.messaging.enums.FriendInvitationStatus;
import lombok.Data;

import java.util.Date;

@Data
public class FriendInvitation {
    private int id;
    private int senderUserId;
    private int receiverUserId;
    private Date sendTime;
    private String message;
    private FriendInvitationStatus status;
}
