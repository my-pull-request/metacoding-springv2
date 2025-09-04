package com.metacoding.springv2.domain.reply;

import lombok.Data; 
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.board.Board;

public class ReplyRequest {
    @Data
    public static class SaveDTO {
        private String comment;
        private Integer boardId;

        public Reply toEntity(User user, Board board) {
            return Reply.builder()
                        .comment(comment)
                        .user(user)
                        .board(board)
                        .build();
        }
    }
}
