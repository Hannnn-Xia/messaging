package com.han.messaging.service;

import com.han.messaging.dao.UserDAO;
import com.han.messaging.dao.UserValidationCodeDAO;
import com.han.messaging.enums.Gender;
import com.han.messaging.enums.StatusCode;
import com.han.messaging.exception.MessagingServiceException;
import com.han.messaging.model.User;
import com.han.messaging.model.UserValidationCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String TEST_USERNAME = "user";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_VALIDATION_CODE = "123123";
    private static final int TEST_USER_ID = 1;

    @Mock
    UserDAO userDAO;

    @Mock
    UserValidationCodeDAO userValidationCodeDAO;

    @InjectMocks
    UserService userService;



    @Test
    public void testRegister_happyCase() throws Exception {
        when(this.userDAO.selectByEmail("fanjianjin@gmail.com")).then(invocationOnMock -> {
            String email = invocationOnMock.getArgument(0);
            System.out.println(email);
            return null;
        });
        when(this.userDAO.selectByUsername("xx")).thenReturn(null);
        doAnswer(invocationOnMock -> {
            User user = invocationOnMock.getArgument(0);
            user.setId(10);
            return null;
        }).when(this.userDAO).insert(any(User.class));
        this.userService.register("xx", "dsfsfddsfsfdsfsf", "dsfsfddsfsfdsfsf", "fanjianjin@gmail.com", Gender.MALE, "", "");

        verify(this.userDAO).selectByEmail("fanjianjin@gmail.com");
        verify(this.userDAO).selectByUsername("xx");
    }

    @Test
    public void testRegister_usernameIsEmpty_throwsMessagingServiceExceptionWithUsernameOrEmailIsEmpty() {
        MessagingServiceException messagingServiceException = assertThrows(MessagingServiceException.class,
                () -> this.userService.register("", "dsfsfddsfsfdsfsf", "dsfsfddsfsfdsfsf", "fanjianjin@gmail.com", Gender.MALE, "", ""));
        assertEquals(StatusCode.USERNAME_OR_EMAIL_IS_EMPTY, messagingServiceException.getStatusCode());
    }

    @Test
    public void testRegister_emailExists_throwsMessagingServiceExceptionWithEmailExistsAlready() throws Exception {
        when(this.userDAO.selectByEmail("fanjianjin@gmail.com")).thenReturn(new User());
        MessagingServiceException messagingServiceException = assertThrows(MessagingServiceException.class,
                () -> this.userService.register("xx", "dsfsfddsfsfdsfsf", "dsfsfddsfsfdsfsf", "fanjianjin@gmail.com", Gender.MALE, "", ""));
        assertEquals(StatusCode.EMAIL_EXISTS_ALREADY, messagingServiceException.getStatusCode());
    }

    @Test
    public void testRegister_usernameExists_throwsMessagingServiceExceptionWithUsernameExistsAlready() throws Exception {
        when(this.userDAO.selectByEmail("fanjianjin@gmail.com")).thenReturn(null);
        when(this.userDAO.selectByUsername("xx")).thenReturn(new User());
        MessagingServiceException messagingServiceException = assertThrows(MessagingServiceException.class,
                () -> this.userService.register("xx", "dsfsfddsfsfdsfsf", "dsfsfddsfsfdsfsf", "fanjianjin@gmail.com", Gender.MALE, "", ""));
        assertEquals(StatusCode.USERNAME_EXISTS_ALREADY, messagingServiceException.getStatusCode());
    }

    @Test
    public void testRegister_passwordsNotMatched_throwsMessagingServiceExeptionWithPasswordsNotMatchedStatusCode() {
        MessagingServiceException messagingServiceException = assertThrows(MessagingServiceException.class,
                () -> this.userService.register("", "xxxxxxxx", "t", "", Gender.MALE, "", ""));
        assertEquals(StatusCode.PASSWORDS_NOT_MATCHED, messagingServiceException.getStatusCode());
    }

    @Test
    public void testActivate_happyCase() throws Exception {
        var user = new User();
        user.setId(TEST_USER_ID);
        user.setUsername(TEST_USERNAME);
        user.setPassword(TEST_PASSWORD);
        when(this.userDAO.selectByUsername(TEST_USERNAME)).thenReturn(user);

        var userValidationCode = new UserValidationCode();
        userValidationCode.setUserId(1);
        userValidationCode.setValidationCode(TEST_VALIDATION_CODE);
        when(this.userValidationCodeDAO.findLatestByUserId(TEST_USER_ID)).thenReturn(userValidationCode);

        this.userService.activate(TEST_USERNAME, TEST_PASSWORD, TEST_VALIDATION_CODE);
    }
}
