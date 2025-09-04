package com.metacoding.springv2.domain.reply;

import lombok.Data; 
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.board.Board;
import com.metacoding.springv2.domain.reply.Reply;
import com.metacoding.springv2.domain.user.User;

public class ReplyResponse {
    @Data
    public static class DTO {
        private Integer id; 
        private String comment;
        private Integer userId;
        private String username;
        private Integer boardId;

        public DTO(Reply reply,Board board,User user) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.userId = user.getId();
            this.username = user.getUsername();
            this.boardId = board.getId();
        }
    }
}
