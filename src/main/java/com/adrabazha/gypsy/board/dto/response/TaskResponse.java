package com.adrabazha.gypsy.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskResponse {

    private String taskName;

    private Integer taskOrder;

    private String taskDescription;

    private UserResponse assignee;

    private List<CommentResponse> comments;
}
