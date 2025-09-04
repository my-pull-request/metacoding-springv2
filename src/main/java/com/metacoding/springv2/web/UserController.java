package com.metacoding.springv2.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.metacoding.springv2.domain.user.UserService;
import com.metacoding.springv2.domain.user.UserRequest;
import com.metacoding.springv2.domain.auth.AuthResponse;
import com.metacoding.springv2.core.util.JWTUtil;
import org.springframework.web.bind.annotation.RequestHeader;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PutMapping("/api/users")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest.UpdateDTO requestDTO,@RequestHeader("Authorization") String jwtToken) {
        String token = jwtToken.replace(JWTUtil.TOKEN_PREFIX, "");
        AuthResponse.DTO responseDTO = userService.회원수정(requestDTO,JWTUtil.verify(token).getUsername());
        return ResponseEntity.ok(responseDTO);
    }
}
