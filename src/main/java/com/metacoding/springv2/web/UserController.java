package com.metacoding.springv2.web;

import com.metacoding.springv2.core.util.Resp;
import com.metacoding.springv2.domain.user.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal User sessionUser, @Valid @RequestBody UserRequest.UpdateDTO requestDTO, Errors errors) {
        var responseDTO = userService.회원수정(requestDTO, sessionUser.getId());
        return Resp.ok(responseDTO);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal User sessionUser, @PathVariable("userId") Integer userId) {
        // 넘길때는 정확히 넘기자, User 객체 혹은 스칼라값
        var responseDTO = userService.회원조회(userId, sessionUser.getId());
        return Resp.ok(responseDTO); // Resp 써야해
    }

}
