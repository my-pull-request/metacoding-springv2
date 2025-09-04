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
        private Integer id;
        private String title;
        private String content;
        private User user;

        public DetailDTO(Board board){
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.user = board.getUser();
        }
    }
}
