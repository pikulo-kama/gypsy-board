package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
