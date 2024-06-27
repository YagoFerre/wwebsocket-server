package com.example.websockets.controller;

import com.example.websockets.domain.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/grupo/public")
    public ChatMessage receivePublicMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/private-message")
    public ChatMessage receivePrivateMessage(@Payload ChatMessage chatMessage) {

        simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver(), "/private", chatMessage);
        return chatMessage;
    }

    @MessageMapping("/add")
    @SendTo("/grupo/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
