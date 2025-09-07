package com.metacoding.springv2.domain.reply;

import lombok.Data;

public class ReplyResponse {
    @Data
    public static class DTO {
        private Integer id;
        private String comment;
        private Integer userId;
        private Integer boardId;
        private String username; // 댓글 화면에 username 필요

        public DTO(Reply reply, String username) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.userId = reply.getUser().getId(); // 아직 lazy loading 이해가 안됐다는 것
            this.boardId = reply.getBoard().getId();
            this.username = username;
        }
    }
}
