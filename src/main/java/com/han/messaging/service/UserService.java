package com.han.messaging.service;

import com.han.messaging.dao.UserDAO;
import com.han.messaging.dao.UserValidationCodeDAO;
import com.han.messaging.enums.Gender;
import com.han.messaging.enums.StatusCode;
import com.han.messaging.exception.MessagingServiceException;
import com.han.messaging.model.User;
import com.han.messaging.model.UserValidationCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private static final Duration LOGIN_TOKEN_EXPIRY = Duration.ofDays(7);
    @Autowired // dependency injection
    private UserDAO userDAO;

    @Autowired
    private UserValidationCodeDAO userValidationCodeDAO;

    public void register(String username,
                         String password,
                         String repeatPassword,
                         String email,
                         Gender gender,
                         String address,
                         String nickname) throws Exception {
        if (!password.equals(repeatPassword)) {
            throw new MessagingServiceException(StatusCode.PASSWORDS_NOT_MATCHED);
        }

        if (username.isEmpty() || email.isEmpty()) {
            throw new MessagingServiceException(StatusCode.USERNAME_OR_EMAIL_IS_EMPTY);
        }

        if (password.length() < 16) {
            throw new MessagingServiceException(StatusCode.PASSWORD_TOO_SHORT);
        }

        if (this.userDAO.selectByEmail(email) != null) {
            throw new MessagingServiceException(StatusCode.EMAIL_EXISTS_ALREADY);
        }

        if (this.userDAO.selectByUsername(username) != null) {
            throw new MessagingServiceException(StatusCode.USERNAME_EXISTS_ALREADY);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setGender(gender);
        user.setAddress(address);
        user.setNickname(nickname);
        user.setRegisterTime(new Date());
        user.setValid(false);

        this.userDAO.insert(user); // setId

        System.out.println(user.getId());
        String validationCode = RandomStringUtils.randomNumeric(6);

        UserValidationCode userValidationCode = new UserValidationCode();
        userValidationCode.setUserId(user.getId());
        userValidationCode.setValidationCode(validationCode);
        this.userValidationCodeDAO.insert(userValidationCode);

        // send a email

    }

    public void activate(String identification, String password, String validationCode) throws MessagingServiceException {

        User user = this.findUserByIdentification(identification);

        if (!password.equals(user.getPassword())) {
            throw new MessagingServiceException(StatusCode.WRONG_PASSWORD);
        }

        UserValidationCode userValidationCode = this.userValidationCodeDAO.findLatestByUserId(user.getId());
        if (!userValidationCode.getValidationCode().equals(validationCode)) {
            throw new MessagingServiceException(StatusCode.WRONG_VALIDATION_CODE);
        }

        this.userDAO.updateToValid(user.getId());
        this.userValidationCodeDAO.deleteByUserId(user.getId());
    }

    public User authenticate(String loginToken) throws MessagingServiceException {
        User user = this.userDAO.selectByLoginToken(loginToken);
        if (user == null || !user.getValid()) {
            throw new MessagingServiceException(StatusCode.INACTIVE_USER);
        }
        if (new Date().getTime() - user.getLastLoginTime().getTime() > LOGIN_TOKEN_EXPIRY.toMillis()) {
            throw new MessagingServiceException(StatusCode.EXPIRED_LOGIN_TOKEN);
        }
        return user;
    }

    public String login(String identification, String password) throws MessagingServiceException {
        User user = this.findUserByIdentification(identification);
        if (!password.equals(user.getPassword())) {
            throw new MessagingServiceException(StatusCode.WRONG_PASSWORD);
        }

        if (!user.getValid()) {
            throw new MessagingServiceException(StatusCode.INACTIVE_USER);
        }

        String loginToken = RandomStringUtils.randomAlphanumeric(128);
        this.userDAO.login(loginToken, new Date(), user.getId());

        return loginToken;
    }

    public User findUserByIdentification(String identification) throws MessagingServiceException {
        User user = this.userDAO.selectByUsername(identification);
        if (user == null) {
            user = this.userDAO.selectByEmail(identification);
        }

        if (user == null) {
            throw new MessagingServiceException(StatusCode.USER_NOT_EXIST);
        }
        return user;
    }

    public List<User> searchUsers(String q) {
        // %12% 12123   xx123 xx12 12xxx
        return this.userDAO.search("%"+q+"%"); // *
    }

    public User findUserById(int userId) throws MessagingServiceException {
        User user = this.userDAO.selectByUserId(userId);
        if (user == null) {
            throw new MessagingServiceException(StatusCode.USER_NOT_EXIST);
        }
        return user;
    }
}
