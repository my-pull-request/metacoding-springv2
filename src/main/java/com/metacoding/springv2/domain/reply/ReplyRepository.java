package com.metacoding.springv2.domain.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.metacoding.springv2.domain.reply.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    Optional<Reply> findById(Integer id);

}
