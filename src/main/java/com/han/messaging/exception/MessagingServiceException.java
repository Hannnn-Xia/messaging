package com.han.messaging.exception;

import com.han.messaging.enums.StatusCode;

public class MessagingServiceException extends Exception {
    StatusCode statusCode;

    public MessagingServiceException(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }


}
