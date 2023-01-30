package com.han.messaging.request;
import lombok.Data;

@Data
public class AcceptInvitationRequest {
    private String loginToken;
    private Integer invitationId;
}
