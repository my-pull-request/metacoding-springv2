package com.metacoding.springv2.admin;

import com.metacoding.springv2.board.*;
import com.metacoding.springv2.core.util.Resp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
