package com.metacoding.springv2.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception403;
import com.metacoding.springv2._core.handler.ex.Exception404;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.DTO> 게시글목록() {
        return boardRepository.findAll().stream()
                .map(BoardResponse.DTO::new)
                .toList();
    }

    @Transactional
    public BoardResponse.DTO 게시글쓰기(BoardRequest.SaveDTO requestDTO, User sessionUser) {
        Board savedBoard = boardRepository.save(requestDTO.toEntity(sessionUser));
        return new BoardResponse.DTO(savedBoard);
    }

    public BoardResponse.DetailDTO 게시글상세(Integer boardId, Integer sessionUserId) {
        Board findBoard = boardRepository.findByIdJoinUserAndReplies(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        return new BoardResponse.DetailDTO(findBoard, sessionUserId);
    }

    // 다른 API에서도 사용가능!! (수정정보라고 하면 재사용성 떨어짐)
    public BoardResponse.DTO 게시글정보(Integer boardId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        return new BoardResponse.DTO(findBoard);
    }

    @Transactional
    public BoardResponse.DTO 게시글수정(BoardRequest.UpdateDTO requestDTO, Integer boardId, Integer sessionUserId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        if (!findBoard.getUser().getId().equals(sessionUserId))
            throw new Exception403("게시글을 수정할 권한이 없습니다");

        findBoard.update(requestDTO.title(), requestDTO.content());
        return new BoardResponse.DTO(findBoard);
    }

    @Transactional
    public void 게시글삭제(Integer boardId, Integer sessionUserId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        if (!findBoard.getUser().getId().equals(sessionUserId))
            throw new Exception403("게시글을 삭제할 권한이 없습니다");

        boardRepository.deleteById(boardId);
    }

    @Transactional
    public void 관리자_게시글삭제(Integer boardId) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        boardRepository.deleteById(boardId);
    }
}