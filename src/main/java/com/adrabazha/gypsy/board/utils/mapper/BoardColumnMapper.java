package com.adrabazha.gypsy.board.utils.mapper;

import com.adrabazha.gypsy.board.domain.sql.BoardColumn;
import com.adrabazha.gypsy.board.domain.sql.Task;
import com.adrabazha.gypsy.board.dto.response.BoardColumnResponse;
import com.adrabazha.gypsy.board.dto.response.TaskReferenceResponse;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoardColumnMapper {

    private final TaskMapper taskMapper;
    private final HashResolverFactory hashResolverFactory;

    public BoardColumnMapper(TaskMapper taskMapper,
                             HashResolverFactory hashResolverFactory) {
        this.taskMapper = taskMapper;
        this.hashResolverFactory = hashResolverFactory;
    }

    public BoardColumnResponse mapBoardColumnToResponse(BoardColumn boardColumn) {
        List<TaskReferenceResponse> taskResponseDtoList = boardColumn.getTasks().stream()
                .sorted(Comparator.comparingInt(Task::getTaskOrder))
                .map(taskMapper::mapTaskToReferenceResponse)
                .collect(Collectors.toList());

        return BoardColumnResponse.builder()
                .columnName(boardColumn.getColumnName())
                .columnHash(hashResolverFactory.obtainHash(boardColumn.getColumnId(), Resolver.BOARD_COLUMN))
                .columnOrder(boardColumn.getColumnOrder())
                .tasks(taskResponseDtoList)
                .build();
    }
}
