package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.BoardColumn;
import com.adrabazha.gypsy.board.domain.Task;
import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.form.ColumnSynchronizationForm;
import com.adrabazha.gypsy.board.dto.form.TaskCreateForm;
import com.adrabazha.gypsy.board.dto.form.TaskUpdateForm;
import com.adrabazha.gypsy.board.dto.response.TaskReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.TaskResponse;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.mapper.TaskMapper;
import com.adrabazha.gypsy.board.repository.TaskRepository;
import com.adrabazha.gypsy.board.utils.resolver.BoardColumnHashResolver;
import com.adrabazha.gypsy.board.utils.resolver.TaskHashResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TaskServiceImpl implements TaskService {

    private final BoardColumnHashResolver boardColumnHashResolver;
    private final TaskHashResolver taskHashResolver;
    private final BoardColumnService boardColumnService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Autowired
    public TaskServiceImpl(BoardColumnHashResolver boardColumnHashResolver, TaskHashResolver taskHashResolver, BoardColumnService boardColumnService,
                           TaskRepository taskRepository, TaskMapper taskMapper, UserService userService) {
        this.boardColumnHashResolver = boardColumnHashResolver;
        this.taskHashResolver = taskHashResolver;
        this.boardColumnService = boardColumnService;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
    }

    private Task findById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new GeneralException("Task not found"));
    }

    @Override
    public TaskResponse getTask(Long taskId) {
        Task task = findById(taskId);
        return taskMapper.mapTaskToResponse(task);
    }

    @Override
    public TaskReferenceResponse createTask(TaskCreateForm taskForm) {
        Long columnId = boardColumnHashResolver.retrieveIdentifier(taskForm.getColumnHash());
        BoardColumn boardColumn = boardColumnService.findById(columnId);
        Integer taskOrder = getNextPosition(boardColumn);

        Task task = Task.builder()
                .taskName(taskForm.getTaskName().trim())
                .taskDescription("")
                .boardColumn(boardColumn)
                .taskOrder(taskOrder)
                .build();
        Task persistedTask = taskRepository.save(task);

        return taskMapper.mapTaskToReferenceResponse(persistedTask);
    }

    @Override
    public void updateTask(TaskUpdateForm taskUpdateForm) {
        Long taskId = taskHashResolver.retrieveIdentifier(taskUpdateForm.getTaskHash());
        User assignee = userService.findUserByUsername(taskUpdateForm.getAssigneeUserName());
        Task task = findById(taskId);
        task = task.toBuilder()
                .taskName(taskUpdateForm.getTaskName())
                .taskDescription(taskUpdateForm.getTaskDescription())
                .userAssigned(assignee)
                .build();

        taskRepository.save(task);
    }

    @Override
    public void synchronizeTasks(ColumnSynchronizationForm columnSynchronizationForm) {
        Long currentColumnId = boardColumnHashResolver.retrieveIdentifier(columnSynchronizationForm.getSourceColumnHash());
        BoardColumn currentColumn = boardColumnService.findById(currentColumnId);

        Long foreignColumnId = boardColumnHashResolver.retrieveIdentifier(columnSynchronizationForm.getTargetColumnHash());
        BoardColumn foreignColumn = boardColumnService.findById(foreignColumnId);

        List<Task> persistedTasks = currentColumn.getTasks();

        List<Long> taskIdList = columnSynchronizationForm.getTaskHashList().stream()
                .map(taskHashResolver::retrieveIdentifier)
                .collect(Collectors.toList());
        Map<Long, Integer> prioritizeTaskIdList = prioritizeTaskIdList(taskIdList);

        persistedTasks.forEach(task -> {
            if (prioritizeTaskIdList.containsKey(task.getTaskId())) {
                Integer taskOrder = prioritizeTaskIdList.get(task.getTaskId());
                task.setTaskOrder(taskOrder);
            } else {
                task.setBoardColumn(foreignColumn);
                task.setTaskOrder(getNextPosition(foreignColumn));
            }
        });

        boardColumnService.save(currentColumn);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    private Integer getNextPosition(BoardColumn boardColumn) {
        Optional<Task> task = boardColumn.getTasks().stream().max(Comparator.comparing(Task::getTaskOrder));
        int nextPosition = 0;

        if (task.isPresent()) {
            nextPosition = task.get().getTaskOrder() + 1;
        }
        return nextPosition;
    }

    private Map<Long, Integer> prioritizeTaskIdList(List<Long> taskIdList) {
        return IntStream.range(0, taskIdList.size()).boxed()
                .collect(Collectors.toMap(taskIdList::get, i -> i));
    }
}
