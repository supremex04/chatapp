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
        // /topic/.. , /queue/.. , /user/.. messages go from server to clients, clients can subscribe to them
        config.enableSimpleBroker("/topic", "/queue", "/user");
        // defines the prefix for messages that the client sends to the server.
        // when client sends messages with dest. /app/... spring routes them to our controller methods (@MessageMapping)
        config.setApplicationDestinationPrefixes("/app");
        // allows you to send messages directly to specific users
        // special case for private messaging
        config.setUserDestinationPrefix("/user");
    }


    // STOMP (Simple text oriented messaging protocol)
    // protocol on top of websockets that makes messaging structured
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // addEndpoint("/ws") defines the entry point for WebSocket connections
        // CORS, by default frontend (localhost:5173) cannot call backend (localhost:8080)
        // setAllowedOrigins() tells which frontends to connect from
        // without this react app will be blocked by browser
        // defines the endpoint that clients will connect to for establishing a WebSocket connection
        // as frontend is made with React vite, 5173, react-3000
        registry.addEndpoint("/ws").
                setAllowedOrigins("http://localhost:5173", "http://localhost:3000")
                .withSockJS();
        // enables SockJS fallback for browsers that don’t support WebSockets
        // if WebSockets aren’t available, it falls back to HTTP-based techniques like long polling
    }

}
