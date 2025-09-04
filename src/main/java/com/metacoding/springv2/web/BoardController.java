package com.metacoding.springv2.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.metacoding.springv2.domain.board.BoardService;
import com.metacoding.springv2.domain.board.BoardRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import com.metacoding.springv2.core.util.JWTUtil;
import com.metacoding.springv2.domain.board.BoardResponse;
import java.util.List;
import com.metacoding.springv2.domain.user.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/boards")
    public ResponseEntity<?> findAll() {
        List<BoardResponse.DTO> responseDTO = boardService.게시글목록();
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/api/boards")
    public ResponseEntity<?> save(Authentication authentication,@Valid @RequestBody BoardRequest.SaveDTO requestDTO,Errors errors) {
        User user = (User) authentication.getPrincipal();
        BoardResponse.DTO responseDTO = boardService.게시글쓰기(requestDTO,user);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/api/boards/{boardId}")
    public ResponseEntity<?> findById(Authentication authentication, @PathVariable Integer boardId) {
        User user = (User) authentication.getPrincipal();
        BoardResponse.DetailDTO responseDTO = boardService.게시글상세(boardId,user);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/api/boards/{boardId}/edit")
    public ResponseEntity<?> updateInfo(Authentication authentication, @PathVariable Integer boardId) {
        User user = (User) authentication.getPrincipal();
        BoardResponse.DTO responseDTO = boardService.게시글수정정보(boardId,user);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/api/boards/{boardId}")
    public ResponseEntity<?> update(Authentication authentication, @PathVariable Integer boardId,@Valid @RequestBody BoardRequest.UpdateDTO requestDTO,Errors errors) {
        User user = (User) authentication.getPrincipal();
        BoardResponse.DTO responseDTO = boardService.게시글수정(requestDTO,boardId,user);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/api/boards/{boardId}")
    public ResponseEntity<?> deleteById(Authentication authentication, @PathVariable Integer boardId) {
        User user = (User) authentication.getPrincipal();
        boardService.게시글삭제(boardId,user);
        return ResponseEntity.ok("게시글 삭제 완료");
    }
}   
