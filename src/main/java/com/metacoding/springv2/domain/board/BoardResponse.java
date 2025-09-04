package com.metacoding.springv2.domain.board;

import lombok.Data;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.domain.board.Board;

public class BoardResponse {
    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String content;

        public DTO(Board board){
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }
    @Data
    public static class DetailDTO{
        private Integer boardId;
        private String title;
        private String content;
        private Integer userId;
        private String username;
        private Boolean isBoardOwner;

        public DetailDTO(Board board, Boolean isBoardOwner){
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();
            this.isBoardOwner = isBoardOwner;
        }
    }
}
