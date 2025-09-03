package com.metacoding.springv2.domain.board;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp; 
import jakarta.persistence.*;
import com.metacoding.springv2.domain.reply.Reply;
import com.metacoding.springv2.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity  
@Table(name="board_tb") 
public class Board {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)  
    private Integer id;
    @Column(length = 30,nullable = false)
    private  String title;
    @Column(length = 300,nullable = false)
    private  String content;

    @CreationTimestamp 
    private  Timestamp createdAt;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id") 
    private User user; 

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY) 
    private List<Reply> replies = new ArrayList<>();; 

    @Builder
    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}