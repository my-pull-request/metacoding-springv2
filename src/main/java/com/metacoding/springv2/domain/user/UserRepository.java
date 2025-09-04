package com.metacoding.springv2.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import com.metacoding.springv2.domain.user.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    Optional<User> findById(Integer id);
}
