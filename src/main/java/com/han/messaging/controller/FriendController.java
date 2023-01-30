package com.han.messaging.controller;

import com.han.messaging.enums.StatusCode;
import com.han.messaging.exception.MessagingServiceException;
import com.han.messaging.model.FriendInvitation;
import com.han.messaging.model.User;
import com.han.messaging.request.AcceptInvitationRequest;
import com.han.messaging.request.AddFriendRequest;
import com.han.messaging.response.*;
import com.han.messaging.service.FriendService;
import com.han.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @PostMapping("/add")
    public CommonResponse addFriend(@RequestBody AddFriendRequest addFriendRequest) throws MessagingServiceException {
        User sender = this.userService.authenticate(addFriendRequest.getLoginToken());
        User receiver = this.userService.findUserByIdentification(addFriendRequest.getIdentification());
        this.friendService.addFriendInvitation(sender, receiver, addFriendRequest.getMessage());
        return new CommonResponse(StatusCode.OK);
    }

    @GetMapping("/listInvitations")
    public ListFriendInvitationsResponse listInvitations(@RequestParam String loginToken) throws MessagingServiceException {
        User receiver = this.userService.authenticate(loginToken);
        List<FriendInvitation> friendInvitations = this.friendService.listInvitations(receiver);
        List<FriendInvitationResponse> friendInvitationResponses = new ArrayList<>(friendInvitations.size());
        for (FriendInvitation friendInvitation : friendInvitations) {
            User sender = this.userService.findUserById(friendInvitation.getSenderUserId());
            FriendInvitationResponse response = new FriendInvitationResponse(friendInvitation, sender);
            friendInvitationResponses.add(response);
        }
        return new ListFriendInvitationsResponse(friendInvitationResponses);
    }

    @PostMapping("/accept")
    public CommonResponse acceptInvitation(@RequestBody AcceptInvitationRequest acceptInvitationRequest) throws MessagingServiceException{
        User receiver = this.userService.authenticate(acceptInvitationRequest.getLoginToken());
        this.friendService.accpetById(acceptInvitationRequest.getInvitationId());
        return new CommonResponse(StatusCode.OK);
    }

    @PostMapping("/reject")
    public CommonResponse rejectInvitation(@RequestBody AcceptInvitationRequest acceptInvitationRequest) throws MessagingServiceException{
        User receiver = this.userService.authenticate(acceptInvitationRequest.getLoginToken());
        // need to check if the status is pending, or if any record is accepted
        this.friendService.rejectById(acceptInvitationRequest.getInvitationId());
        return new CommonResponse(StatusCode.OK);
    }


    @GetMapping("/listFriends") // status='ACCEPTED'
    public ListFriendsResponse listFriends(@RequestParam String loginToken) throws MessagingServiceException{
     User receiver = this.userService.authenticate(loginToken);
      List<FriendInvitation> friendInvitations = this.friendService.findFriends(receiver.getId());
      List<UserResponse> friends = new ArrayList<>(friendInvitations.size());
      for (FriendInvitation friendInvitation : friendInvitations) {
          User sender = this.userService.findUserById(friendInvitation.getSenderUserId());
          UserResponse friend = new UserResponse(sender);
          System.out.println(sender.getUsername());
          friends.add(friend);
      }
      return new ListFriendsResponse(friends);
    }

    // @PostMapping   Delete record,  or reconstruct the table
}
