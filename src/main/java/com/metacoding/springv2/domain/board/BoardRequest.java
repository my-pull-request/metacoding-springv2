package com.metacoding.springv2.domain.board;

import lombok.Data;
import com.metacoding.springv2.domain.board.Board;
import com.metacoding.springv2.domain.user.User;

public class BoardRequest {
    @Data
    public static class SaveDTO{
        private String title;
        private String content;

        public Board toEntity(User user){
            return Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
        }
    }
}
