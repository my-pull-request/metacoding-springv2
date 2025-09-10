package com.metacoding.springv2.reply;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2.core.util.Resp;
import com.metacoding.springv2.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/replies")
@RequiredArgsConstructor
@RestController
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<?> save(@AuthenticationPrincipal User sessionUser,
            @Valid @RequestBody ReplyRequest.SaveDTO requestDTO, Errors errors) {
        var responseDTO = replyService.댓글쓰기(requestDTO, sessionUser);
        return Resp.ok(responseDTO);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteById(@AuthenticationPrincipal User sessionUser,
            @PathVariable("replyId") Integer replyId) {
        replyService.댓글삭제(replyId, sessionUser.getId());
        return Resp.ok(null);
    }
}
