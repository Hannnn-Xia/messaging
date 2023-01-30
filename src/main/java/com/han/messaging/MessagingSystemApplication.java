package com.han.messaging;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class MessagingSystemApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MessagingSystemApplication.class, args);
    }


}
