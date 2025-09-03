package com.metacoding.springv2.domain.auth;

import lombok.Data;
import com.metacoding.springv2.domain.user.User;

public class AuthRequest {

    @Data
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;
        private String roles;
        
        public User toEntity(String encPassword){
            return User.builder()
                .username(username)
                .password(encPassword)
                .email(email)
                .roles(roles)
                .build();
        }
    }
    
}
