package com.metacoding.springv2.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import com.metacoding.springv2.domain.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
}
