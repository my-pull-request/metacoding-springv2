package com.metacoding.springv2.web;

import com.metacoding.springv2.core.util.Resp;
import com.metacoding.springv2.domain.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {
    private final BoardService boardService;

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("boardId") Integer boardId) {
        boardService.관리자_게시글삭제(boardId);
        return Resp.ok(null);
    }
}
