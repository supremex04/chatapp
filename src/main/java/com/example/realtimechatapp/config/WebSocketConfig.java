package com.example.realtimechatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
// clients send messages to message broker, and it delivers them to whoever is subscribed
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){

        //enable simple broker for group and user-specific/private chat
        // simple broker means spring itself acts  post office without using externals like kafka/rabbitmq
        config.enableSimpleBroker("/topic", "/queue", "/user");
        // defines the prefix for messages that the client sends to the server.
        config.setApplicationDestinationPrefixes("/app");
        // allows you to send messages directly to specific users
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // defines the endpoint that clients will connect to for establishing a WebSocket connection
        // as frontend is made with React vite, 5173, react-3000
        registry.addEndpoint("/ws").
                setAllowedOrigins("http://localhost:5173", "http://localhost:3000")
                .withSockJS();
        // enables SockJS fallback for browsers that don’t support WebSockets
        // if WebSockets aren’t available, it falls back to HTTP-based techniques like long polling
    }

}
