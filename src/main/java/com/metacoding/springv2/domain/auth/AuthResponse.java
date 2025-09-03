package com.metacoding.springv2.domain.auth;

import lombok.Data;
import com.metacoding.springv2.domain.user.User;

public class AuthResponse {
    @Data
    public static class DTO {
        private Integer id;
        private String username;
        private String email;
        private String roles;

        public DTO(User user){
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.roles = user.getRoles();
        }
    }
}
