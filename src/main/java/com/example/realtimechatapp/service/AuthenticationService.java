package com.example.realtimechatapp.service;

import com.example.realtimechatapp.dto.LoginRequestDTO;
import com.example.realtimechatapp.dto.LoginResponseDTO;
import com.example.realtimechatapp.dto.RegisterRequestDTO;
import com.example.realtimechatapp.dto.UserDTO;
import com.example.realtimechatapp.jwt.JwtService;
import com.example.realtimechatapp.model.User;
import com.example.realtimechatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    public UserDTO signup(RegisterRequestDTO registerRequestDTO){
        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("Username is already in use");
        }

        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setEmail(registerRequestDTO.getEmail());

        User savedUser = userRepository.save(user);
        return convertToUserDTO(user);


    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Username not found"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        String jwtToken = jwtService.generateToken(user);

        return LoginResponseDTO.builder()
                .token(jwtToken)
                .userDTO(convertToUserDTO(user))
                .build();
    }



    public UserDTO convertToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());

        return userDTO;

    }

}
