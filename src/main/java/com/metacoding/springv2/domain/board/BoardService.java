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
import com.metacoding.springv2.core.handler.ex.Exception404;
import com.metacoding.springv2.core.handler.ex.Exception403;

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

    public BoardResponse.DetailDTO 게시글상세(Integer boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        Boolean isBoardOwner = (board.getUser().getId() == user.getId());
        BoardResponse.DetailDTO reponseDTO = new BoardResponse.DetailDTO(board,isBoardOwner);
        return reponseDTO;
    }

    public BoardResponse.DTO 게시글수정정보(Integer boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        if(board.getUser().getId() != user.getId()) {
            throw new Exception403("게시글을 수정할 권한이 없습니다.");
        }
        BoardResponse.DTO reponseDTO = new BoardResponse.DTO(board);
        return reponseDTO;
    }

    @Transactional
    public BoardResponse.DTO 게시글수정(BoardRequest.UpdateDTO requestDTO, Integer boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        if(board.getUser().getId() != user.getId()) {
            throw new Exception403("게시글을 수정할 권한이 없습니다.");
        }
        board.update(requestDTO.getTitle(), requestDTO.getContent());
        return new BoardResponse.DTO(board);
    }

    @Transactional
    public void 게시글삭제(Integer boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        if(board.getUser().getId() != user.getId()) {
            throw new Exception403("게시글을 삭제할 권한이 없습니다.");
        }
        boardRepository.deleteById(boardId);
    }
}