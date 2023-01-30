package com.han.messaging.service;

import com.han.messaging.dao.FriendDAO;
import com.han.messaging.enums.FriendInvitationStatus;
import com.han.messaging.enums.StatusCode;
import com.han.messaging.exception.MessagingServiceException;
import com.han.messaging.model.FriendInvitation;
import com.han.messaging.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class FriendService {

    @Autowired
    private FriendDAO friendDAO;

    private static final Duration INVITATION_FREEZING_PERIOD = Duration.ofDays(7);

    public void addFriendInvitation(User sender, User receiver, String message) throws MessagingServiceException {

        // validation
        // 1. sender and receiver are friends already, throw an exception
        // 2. anti-spam:
        //    - if sender and receiver have an existing pending invitation, throw an exception
        //    - if sender has sent an invitation, but it was rejected by the receiver, stop them from sending a new
        //      request within 1/7 days  (make sure that pending and accpeted are the only one)
        FriendInvitation existedInvitation = this.friendDAO.checkExisted(receiver.getId(), sender.getId());
        if (existedInvitation != null) {
            FriendInvitationStatus status = existedInvitation.getStatus();
            switch (status) {
                case ACCEPTED:
                    throw new MessagingServiceException(StatusCode.ALREADY_FRIEND);
                case PENDING:
                    throw new MessagingServiceException(StatusCode.INVITATION_PENDING);
                case REJECTED:
                    // if time until last sent time < 7 days, forbidden, else new request
                    if (new Date().getTime() - existedInvitation.getSendTime().getTime() < INVITATION_FREEZING_PERIOD.toMillis()) {
                        throw new MessagingServiceException(StatusCode.INVITATION_REJECTED);
                    }
            }
        }

        FriendInvitation friendInvitation = new FriendInvitation();
        friendInvitation.setMessage(message);
        friendInvitation.setStatus(FriendInvitationStatus.PENDING);
        friendInvitation.setSendTime(new Date());
        friendInvitation.setSenderUserId(sender.getId());
        friendInvitation.setReceiverUserId(receiver.getId());

        this.friendDAO.insert(friendInvitation);
    }

    public List<FriendInvitation> listInvitations(User receiver) {
        return this.friendDAO.showInvitation(receiver.getId());
    }

    public void accpetById(int invitationId) throws MessagingServiceException {
        FriendInvitation invitation = this.findInvitationById(invitationId);
        this.friendDAO.acceptById(invitationId);
    }

    public void rejectById(int invitationId) throws MessagingServiceException {
        FriendInvitation invitation = this.findInvitationById(invitationId);
        this.friendDAO.rejectById(invitationId);
    }
    public FriendInvitation findInvitationById(int invitationId) throws MessagingServiceException {
        FriendInvitation invitation = this.friendDAO.selectByInvitationId(invitationId);
        if (invitation == null) {
            throw new MessagingServiceException(StatusCode.INVITATION_NOT_EXIST);
        }
        return invitation;
    }

    public List<FriendInvitation> findFriends(Integer receiverId) {
        return this.friendDAO.findFriends(receiverId);
    }
}
