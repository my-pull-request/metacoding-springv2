package com.metacoding.springv2.domain.reply;

import com.metacoding.springv2.core.handler.ex.Exception403;
import com.metacoding.springv2.core.handler.ex.Exception404;
import com.metacoding.springv2.domain.board.Board;
import com.metacoding.springv2.domain.board.BoardRepository;
import com.metacoding.springv2.domain.reply.Reply;
import com.metacoding.springv2.domain.reply.ReplyRepository;
import com.metacoding.springv2.domain.reply.ReplyRequest;
import com.metacoding.springv2.domain.reply.ReplyResponse;
import com.metacoding.springv2.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service   
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ReplyResponse.DTO 댓글쓰기(ReplyRequest.SaveDTO requestDTO, User user) {
        Board board = boardRepository.findById(requestDTO.getBoardId()).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        Reply reply = requestDTO.toEntity(user, board);
        replyRepository.save(reply);
        return new ReplyResponse.DTO(reply,board,user);
    }

    @Transactional
    public void 댓글삭제(Integer replyId, User user) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new Exception404("댓글을 찾을 수 없습니다"));
        if(reply.getUser().getId() != user.getId()) {
            throw new Exception403("댓글을 삭제할 권한이 없습니다");
        }
        replyRepository.deleteById(replyId);
    }
}