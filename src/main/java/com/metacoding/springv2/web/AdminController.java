package com.metacoding.springv2.web;

import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import com.metacoding.springv2.domain.board.BoardService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AdminController {
    private final BoardService boardService;

    @DeleteMapping("/api/admin/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Integer boardId) {
        boardService.관리자게시글삭제(boardId);
        return ResponseEntity.ok("게시글 삭제 완료");
    }
}
