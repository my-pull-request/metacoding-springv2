package com.metacoding.springv2.domain.board;

import com.metacoding.springv2.domain.reply.Reply;
import lombok.Data;

import java.util.List;

public class BoardResponse {
    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String content;

        public DTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }

    @Data
    public static class DetailDTO {
        private Integer boardId;
        private String title;
        private String content;
        private Integer userId;
        private String username;
        private Boolean isBoardOwner;
        private List<ReplyDTO> replies;

        public DetailDTO(Board board, Integer sessionUserId) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();
            this.isBoardOwner = sessionUserId.equals(board.getUser().getId());
            this.replies = board.getReplies().stream()
                    .map(reply -> new ReplyDTO(reply, sessionUserId))
                    .toList();
        }

        @Data
        public class ReplyDTO {
            private Integer id;
            private String username;
            private String comment;
            private Boolean isOwner;

            public ReplyDTO(Reply reply, Integer sessionUserId) {
                this.id = reply.getId();
                this.username = reply.getUser().getUsername();
                this.comment = reply.getComment();
                this.isOwner = sessionUserId.equals(reply.getUser().getId());
            }
        }

    }
}
