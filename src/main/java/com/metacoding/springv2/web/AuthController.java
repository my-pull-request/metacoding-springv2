package com.metacoding.springv2.web;

import org.springframework.web.bind.annotation.PostMapping;
import com.metacoding.springv2.domain.auth.AuthResponse;
import com.metacoding.springv2.domain.auth.AuthRequest;
import com.metacoding.springv2.domain.user.UserService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody AuthRequest.JoinDTO requestDTO) {
       AuthResponse.DTO responseDTO = userService.회원가입(requestDTO);
       return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest.LoginDTO requestDTO) {
        System.out.println("requestDTO 확인 : " + requestDTO);
        String jwtToken = userService.로그인(requestDTO);
        return ResponseEntity.ok(jwtToken);
    }
}