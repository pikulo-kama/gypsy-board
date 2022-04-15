package com.adrabazha.gypsy.board.mapper;

import com.adrabazha.gypsy.board.domain.Comment;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.response.CommentResponse;
import com.adrabazha.gypsy.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    private final Map<Long, String> userIdFullNameMap;

    @Autowired
    public CommentMapper(UserService userService) {
        userIdFullNameMap = userService.findAll().stream()
                .collect(Collectors.toMap(User::getUserId, User::getFullName));
    }

    public CommentResponse mapCommentToResponse(Comment comment) {
        return CommentResponse.builder()
                .body(comment.getBody())
                .authorFullName(userIdFullNameMap.get(comment.getAuthorId()))
                .commentedAt(comment.getCreationDate())
                .wasEdited(comment.getEdited())
                .build();
    }
}
