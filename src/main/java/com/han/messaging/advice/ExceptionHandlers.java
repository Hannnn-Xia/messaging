package com.han.messaging.advice;

import com.han.messaging.exception.MessagingServiceException;
import com.han.messaging.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandlers {
    //model view controller
    @ExceptionHandler(MessagingServiceException.class)
    @ResponseBody
    public ResponseEntity<CommonResponse> handleMessagingServiceException(MessagingServiceException messagingServiceException) {
        //generic type
        var responseEntity = new ResponseEntity<CommonResponse>(
                new CommonResponse(messagingServiceException.getStatusCode()),
                messagingServiceException.getStatusCode().getHttpStatus());
        return responseEntity;

    }
}
