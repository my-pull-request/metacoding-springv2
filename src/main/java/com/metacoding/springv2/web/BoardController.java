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
    public ResponseEntity<?> save(@RequestHeader("Authorization") String jwtToken, @RequestBody BoardRequest.SaveDTO requestDTO) {
        String token = jwtToken.replace(JWTUtil.TOKEN_PREFIX, "");
        User user = JWTUtil.verify(token);
        BoardResponse.DTO responseDTO = boardService.게시글쓰기(requestDTO,user);
        return ResponseEntity.ok(responseDTO);
    }
}   
