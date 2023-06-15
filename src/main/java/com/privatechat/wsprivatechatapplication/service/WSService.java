package com.privatechat.wsprivatechatapplication.service;

import com.privatechat.wsprivatechatapplication.dto.Message;
import com.privatechat.wsprivatechatapplication.dto.ResponseMessage;
import com.privatechat.wsprivatechatapplication.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WSService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationService notificationService;
    private final UserService userService;

    public ResponseMessage sendPrivateMessage(Message message) throws InterruptedException {
        Thread.sleep(1000);

        UserDTO sender = userService.getByUsername(message.senderUsername());
        String senderPicture = sender.picture();
        int senderId = sender.id();
        var responseMessage = new ResponseMessage(senderId, message.senderUsername(), message.body(), senderPicture);

        notificationService.sendPrivateNotification(message.recipientId());
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.recipientId()), "/chat/private-message", responseMessage);
        return responseMessage;
    }
}
