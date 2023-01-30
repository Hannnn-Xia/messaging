package com.han.messaging.controller;

import com.han.messaging.enums.StatusCode;
import com.han.messaging.request.ActivateUserRequest;
import com.han.messaging.request.RegisterUserRequest;
import com.han.messaging.request.UserLoginRequest;
import com.han.messaging.response.CommonResponse;
import com.han.messaging.response.SearchUserResponse;
import com.han.messaging.response.UserLoginResponse;
import com.han.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public CommonResponse register(@RequestBody RegisterUserRequest registerUserRequest) throws Exception {

        this.userService.register(registerUserRequest.getUsername(),
                registerUserRequest.getPassword(),
                registerUserRequest.getRepeatPassword(),
                registerUserRequest.getEmail(),
                registerUserRequest.getGender(),
                registerUserRequest.getAddress(),
                registerUserRequest.getNickname());
        return new CommonResponse(StatusCode.OK);
    }

    @PostMapping("/activate")
    public CommonResponse activate(@RequestBody ActivateUserRequest activateUserRequest) throws Exception {
        this.userService.activate(activateUserRequest.getIdentification(),
                activateUserRequest.getPassword(),
                activateUserRequest.getValidationCode());

        return new CommonResponse(StatusCode.OK);
    }

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest userLoginRequest) throws Exception {
        String loginToken = this.userService.login(userLoginRequest.getIdentification(),
                userLoginRequest.getPassword());
        return new UserLoginResponse(loginToken);
    }

    @GetMapping("/search")
    public SearchUserResponse search(@RequestParam String q) {
        return new SearchUserResponse(this.userService.searchUsers(q));
    }
}
