package com.metacoding.springv2.domain.board;

import com.metacoding.springv2.domain.reply.Reply;

import java.util.List;

public class BoardResponse {

    public record DTO(
            Integer id,
            String title,
            String content
    ) {
        public DTO(Board board) {
            this(
                board.getId(),
                board.getTitle(),
                board.getContent()
            );
        }
    }

    public record DetailDTO(
            Integer boardId,
            String title,
            String content,
            Integer userId,
            String username,
            Boolean isBoardOwner,
            List<ReplyDTO> replies
    ) {
        public DetailDTO(Board board, Integer sessionUserId) {
            this(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getId(),
                board.getUser().getUsername(),
                sessionUserId.equals(board.getUser().getId()),
                board.getReplies().stream()
                        .map(reply -> new ReplyDTO(reply, sessionUserId))
                        .toList()
            );
        }

        public record ReplyDTO(
                Integer id,
                String username,
                String comment,
                Boolean isOwner
        ) {
            public ReplyDTO(Reply reply, Integer sessionUserId) {
                this(
                    reply.getId(),
                    reply.getUser().getUsername(),
                    reply.getComment(),
                    sessionUserId.equals(reply.getUser().getId())
                );
            }
        }
    }
}
