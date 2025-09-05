package com.metacoding.springv2.web;

import com.metacoding.springv2.domain.auth.AuthResponse;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.user.UserRequest;
import com.metacoding.springv2.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PutMapping("/api/users")
    public ResponseEntity<?> updateUser(Authentication authentication,@Valid @RequestBody UserRequest.UpdateDTO requestDTO,Errors errors) {
        User user = (User) authentication.getPrincipal();
        AuthResponse.DTO responseDTO = userService.회원수정(requestDTO,user.getUsername());
        return ResponseEntity.ok(responseDTO);
    }

    
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<?> getUser(Authentication authentication,@PathVariable Integer userId) {
        User user = (User) authentication.getPrincipal();
        AuthResponse.DTO responseDTO = userService.회원조회(userId,user.getId());
        return ResponseEntity.ok(responseDTO);
    }

}
