package com.metacoding.springv2.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    Optional<Board> findById(Integer id);

    @Query("SELECT b FROM Board b JOIN FETCH b.user LEFT JOIN FETCH b.replies WHERE b.id = :boardId")
    Optional<Board> findByIdJoinUserAndReply(Integer boardId);

}