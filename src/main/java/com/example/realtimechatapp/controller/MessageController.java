package com.example.realtimechatapp.controller;


import com.example.realtimechatapp.model.ChatMessage;
import com.example.realtimechatapp.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
// to get private message from repository
// this class exposes a REST API endpoint for retrieving private messages between two users from the database
public class MessageController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // @RequestParam tells Spring to get these vslues (user1, user2) from the url query parameters
    public ResponseEntity<List<ChatMessage>> getPrivateMessages(@RequestParam String user1, @RequestParam String user2){
        // HTTP Request → Controller → Repository (JPQL → SQL → DB) → List<Message> → JSON Response
        List<ChatMessage> messages = chatMessageRepository.findPrivateMessagesBetweenTwoUsers(user1, user2);
        return ResponseEntity.ok(messages);
    }

}
