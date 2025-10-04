package com.example.realtimechatapp.controller;

import com.example.realtimechatapp.model.ChatMessage;
import com.example.realtimechatapp.repository.ChatMessageRepository;
import com.example.realtimechatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // websocket destination
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        if(userService.userExists(chatMessage.getSender())){
            // store username in session
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            userService.setUserOnlineStatus(chatMessage.getSender(), true);
            System.out.println("User added successfully "+ chatMessage.getSender() + " with session ID"+ headerAccessor.getSessionId());
            chatMessage.setTimeStamp(LocalDateTime.now());

            if(chatMessage.getContent() == null){
                chatMessage.setContent("");
            }
            return chatMessageRepository.save(chatMessage);
        }
        return null;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        if(userService.userExists(chatMessage.getSender())){
            if(chatMessage.getTimeStamp() == null){
                chatMessage.setTimeStamp(LocalDateTime.now());
            }
            if(chatMessage.getContent() == null){
                chatMessage.setContent("");
            }
            return chatMessageRepository.save(chatMessage);
        }
        return null;
    }

    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){

    }
}
