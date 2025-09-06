package com.metacoding.springv2.domain.reply;

import com.metacoding.springv2.domain.board.Board;
import com.metacoding.springv2.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "reply_tb")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 100, nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Reply(String comment, User user, Board board) {
        this.comment = comment;
        this.user = user;
        this.board = board;
    }
}