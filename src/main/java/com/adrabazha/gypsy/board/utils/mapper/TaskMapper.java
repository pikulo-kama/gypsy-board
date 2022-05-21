package com.adrabazha.gypsy.board.utils.mapper;

import com.adrabazha.gypsy.board.domain.sql.Task;
import com.adrabazha.gypsy.board.dto.response.TaskReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.TaskResponse;
import com.adrabazha.gypsy.board.dto.response.UserResponse;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class TaskMapper {

    private final UserMapper userMapper;
    private final HashResolverFactory hashResolverFactory;

    @Autowired
    public TaskMapper(UserMapper userMapper,
                      HashResolverFactory hashResolverFactory) {
        this.userMapper = userMapper;
        this.hashResolverFactory = hashResolverFactory;
    }

    public TaskReferenceResponse mapTaskToReferenceResponse(Task task) {
        return TaskReferenceResponse.builder()
                .taskName(task.getTaskName())
                .taskHash(hashResolverFactory.obtainHash(task.getTaskId(), Resolver.TASK))
                .build();
    }

    public TaskResponse mapTaskToResponse(Task task) {
        UserResponse assignee;

        if (nonNull(task.getUserAssigned())) {
            assignee = userMapper.mapUserToResponse(task.getUserAssigned());
        } else {
            assignee = UserResponse.builder()
                    .username("")
                    .fullName("")
                    .build();
        }

        return TaskResponse.builder()
                .taskName(task.getTaskName())
                .taskDescription(task.getTaskDescription())
                .taskOrder(task.getTaskOrder())
                .assignee(assignee)
                .build();
    }
}
