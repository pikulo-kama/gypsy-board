package com.adrabazha.gipsy.board.repository;

import com.adrabazha.gipsy.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByUsernameContains(String username);
}
