package com.metacoding.springv2.domain.reply;

import lombok.Data; 
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.board.Board;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;


public class ReplyRequest {
    @Data
    public static class SaveDTO {
        @Size(min = 1, max = 100,message = "댓글은 1자 이상 100자 이하로 입력해주세요")
        @NotEmpty(message = "댓글을 입력해주세요")
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
