package com.example.realtimechatapp.listner;

import com.example.realtimechatapp.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.logging.Logger;

public class WebSocketListener {
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketListener.class);

    @EventListener
    public void handleWebsocketConnectListener(SessionConnectedEvent event){
        logger.info("Connected to websocket");

    }

    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    }




}
