package com.metacoding.springv2.domain.board;

import lombok.Data;
import com.metacoding.springv2.domain.board.Board;
import com.metacoding.springv2.domain.user.User;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;


public class BoardRequest {
    @Data
    public static class SaveDTO{
        @Size(min = 1, max = 30,message = "제목은 1자 이상 30자 이하로 입력해주세요")
        @NotEmpty(message = "제목을 입력해주세요")
        private String title;
        @Size(min = 1, max = 300,message = "내용은 1자 이상 300자 이하로 입력해주세요")
        @NotEmpty(message = "내용을 입력해주세요")
        private String content;

        public Board toEntity(User user){
            return Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
        }
    }

    @Data
    public static class UpdateDTO{
        @Size(min = 4, max = 20,message = "제목은 1자 이상 30자 이하로 입력해주세요")
        @NotEmpty(message = "제목을 입력해주세요")
        private String title;
        @Size(min = 4, max = 20,message = "내용은 1자 이상 300자 이하로 입력해주세요")
        @NotEmpty(message = "내용을 입력해주세요")
        private String content;
    }
}
