package com.han.messaging.response;

import com.han.messaging.enums.StatusCode;

import java.util.List;

public class ListFriendInvitationsResponse extends CommonResponse {

    List<FriendInvitationResponse> friendInvitations;

    public ListFriendInvitationsResponse(List<FriendInvitationResponse> friendInvitations) {
        super(StatusCode.OK);
        this.friendInvitations = friendInvitations;
    }

    public List<FriendInvitationResponse> getFriendInvitations() {
        return friendInvitations;
    }
}
