package com.adrabazha.gipsy.board.repository;

import com.adrabazha.gipsy.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
