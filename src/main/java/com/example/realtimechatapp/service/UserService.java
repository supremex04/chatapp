package com.example.realtimechatapp.service;

import com.example.realtimechatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean userExists(String username){
        return userRepository.existsByUsername(username);
    }

    public void setUserOnlineStatus(String username, Boolean isOnline){
        userRepository.updateUserOnlineStatus(username, isOnline);
    }
}
