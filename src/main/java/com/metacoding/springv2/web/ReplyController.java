package com.metacoding.springv2.web;

import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import com.metacoding.springv2.domain.reply.ReplyRequest;
import com.metacoding.springv2.domain.reply.ReplyResponse;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.reply.ReplyService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.validation.Errors;
import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("/api/replies")
    public ResponseEntity<?> save(Authentication authentication,@Valid @RequestBody ReplyRequest.SaveDTO requestDTO,Errors errors) {
        User user = (User) authentication.getPrincipal();
        ReplyResponse.DTO responseDTO = replyService.댓글쓰기(requestDTO, user);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/api/replies/{replyId}")
    public ResponseEntity<?> deleteById(Authentication authentication,@PathVariable Integer replyId) {
        User user = (User) authentication.getPrincipal();
        replyService.댓글삭제(replyId, user);
        return ResponseEntity.ok("댓글 삭제 완료");
    }
}
