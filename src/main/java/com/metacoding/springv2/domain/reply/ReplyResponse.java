package com.metacoding.springv2.domain.reply;

public class ReplyResponse {

    public record DTO(
            Integer id,
            String comment,
            Integer userId,
            Integer boardId,
            String username
    ) {
        public DTO(Reply reply, String username) {
            this(
                reply.getId(),
                reply.getComment(),
                reply.getUser().getId(),  
                reply.getBoard().getId(),
                username
            );
        }
    }
}
