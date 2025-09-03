package com.metacoding.springv2.web;

import org.springframework.web.bind.annotation.PostMapping;

import com.metacoding.springv2.domain.auth.AuthRequest;
import com.metacoding.springv2.domain.user.UserService;
import com.metacoding.springv2.core.util.Resp;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping("/join")
    public Resp<?> join(@RequestBody AuthRequest.JoinDTO requestDTO) {
        System.out.println("requestDTO : " + requestDTO);
        userService.회원가입(requestDTO);
        return new Resp<>(200, "성공", null);
    }

    // @PostMapping("/login")
    // public Resp<?> login(@RequestBody UserRequest.Login requestDTO) {
    //     String accessToken = userService.로그인(requestDTO);
    //     return new Resp<>(accessToken);
    // }
}