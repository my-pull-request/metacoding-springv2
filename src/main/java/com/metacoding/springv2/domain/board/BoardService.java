package com.metacoding.springv2.domain.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.metacoding.springv2.domain.board.Board;
import com.metacoding.springv2.domain.board.BoardRequest;
import com.metacoding.springv2.domain.board.BoardRepository;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.board.BoardResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.DTO> 게시글목록() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponse.DTO> dtos = boards.stream()
            .map(board -> new BoardResponse.DTO(board))
            .collect(Collectors.toList());
        return dtos;
    }

    @Transactional
    public BoardResponse.DTO 게시글쓰기(BoardRequest.SaveDTO requestDTO, User user) {
        Board board = requestDTO.toEntity(user);
        boardRepository.save(board);
        return new BoardResponse.DTO(board);
    }
}