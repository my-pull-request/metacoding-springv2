package com.metacoding.springv2.domain.reply;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.metacoding.springv2.domain.board.Board;
import com.metacoding.springv2.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity  
@Table(name="reply_tb") 
public class Reply {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY) 
    private Integer id;
    @Column(length = 100,nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id") 
    private User user; 

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "board_id") 
    private Board board; 

    @CreationTimestamp 
    private  Timestamp createdAt;

    @Builder
    public Reply(String comment, User user, Board board) {
        this.comment = comment;
        this.user = user;
        this.board = board;
    }   
}