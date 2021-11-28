package com.adrabazha.gipsy.board.service;

import com.adrabazha.gipsy.board.domain.Comment;
import com.adrabazha.gipsy.board.exception.GeneralException;
import com.adrabazha.gipsy.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment create(Object form) {
        return null;
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Comment not found"));
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment updateById(Long id, Object form) {
        return null;
    }
}
