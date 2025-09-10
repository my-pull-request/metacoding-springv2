package com.metacoding.springv2.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2.core.util.Resp;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal User sessionUser,
            @Valid @RequestBody UserRequest.UpdateDTO requestDTO, Errors errors) {
        var responseDTO = userService.회원수정(requestDTO, sessionUser.getId());
        return Resp.ok(responseDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal User sessionUser,
            @PathVariable("userId") Integer userId) {
        var responseDTO = userService.회원조회(userId, sessionUser.getId());
        return Resp.ok(responseDTO);
    }

}
