package com.example.realtimechatapp.service;

import com.example.realtimechatapp.dto.LoginRequestDTO;
import com.example.realtimechatapp.dto.LoginResponseDTO;
import com.example.realtimechatapp.dto.RegisterRequestDTO;
import com.example.realtimechatapp.dto.UserDTO;
import com.example.realtimechatapp.jwt.JwtService;
import com.example.realtimechatapp.model.User;
import com.example.realtimechatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    // check if username exists - hash password - save user in database
    public UserDTO signup(RegisterRequestDTO registerRequestDTO){
        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("Username is already in use");
        }
        if (registerRequestDTO != null) {
            User user = new User();
            user.setUsername(registerRequestDTO.getUsername());
            // BCrypt hash of password is stored in db
            user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
            user.setEmail(registerRequestDTO.getEmail());

            userRepository.save(user);
            // return a safe version of user as userDTO, without sensitive info.
            return convertToUserDTO(user);
        }
        throw new RuntimeException("Invalid username or password");
    }

    // check username - verify password - create JWT that represents logged in user - return token+ safe user info
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

    // creates cookie with same name JWT but empty and max age 0
    // effectively deleting cookie from browser and logging user out
    public ResponseEntity<String> logout(){

        ResponseCookie responseCookie = ResponseCookie.from("JWT", "")
        .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body("Logged out successfully");
    }

    public Map<String, Object> getOnlineUsers(){
        List<User> usersList = userRepository.findByIsOnlineTrue();
        Map<String, Object> onlineUsers = usersList.stream()
                .collect(Collectors.toMap(User::getUsername, user -> user));
        return onlineUsers;
    }

    public UserDTO convertToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());

        return userDTO;

    }

}
