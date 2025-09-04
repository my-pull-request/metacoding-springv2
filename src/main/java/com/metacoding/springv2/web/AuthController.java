package com.metacoding.springv2.web;

import org.springframework.web.bind.annotation.PostMapping;
import com.metacoding.springv2.domain.auth.AuthResponse;
import com.metacoding.springv2.domain.auth.AuthRequest;
import com.metacoding.springv2.domain.user.UserService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.validation.Errors;
import jakarta.validation.Valid;


@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody AuthRequest.JoinDTO requestDTO,Errors errors) {
       AuthResponse.DTO responseDTO = userService.회원가입(requestDTO);
       return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest.LoginDTO requestDTO,Errors errors) {
        String jwtToken = userService.로그인(requestDTO);
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> getUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.회원중복체크(username));
    }
}