package com.metacoding.springv2.domain.auth;

import lombok.Data;
import com.metacoding.springv2.domain.user.User;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;


public class AuthRequest {

    @Data
    public static class JoinDTO {
        @Size(min = 4, max = 20,message = "유저네임은 4자 이상 20자 이하로 입력해주세요")
        @NotEmpty(message = "유저네임을 입력해주세요")
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요")
        @Size(min = 8, max = 64, message = "비밀번호는 8~60자여야 합니다")
        private String password;
        @Email(message = "이메일 형식이 올바르지 않습니다")
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
    @Data
    public static class LoginDTO {
        @NotEmpty(message = "유저네임을 입력해주세요")
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요")
        private String password;
    }
    
    
}
