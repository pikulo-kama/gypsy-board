package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.BoardColumn;
import com.adrabazha.gypsy.board.domain.sql.Task;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.form.ColumnSynchronizationForm;
import com.adrabazha.gypsy.board.dto.form.TaskCreateForm;
import com.adrabazha.gypsy.board.dto.form.TaskUpdateForm;
import com.adrabazha.gypsy.board.dto.response.TaskReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.TaskResponse;
import com.adrabazha.gypsy.board.utils.mapper.TaskMapper;
import com.adrabazha.gypsy.board.repository.TaskRepository;
import com.adrabazha.gypsy.board.utils.mail.CustomEventPublisher;
import com.adrabazha.gypsy.board.utils.mail.templates.MessageTemplates;
import com.adrabazha.gypsy.board.utils.resolver.BoardColumnHashResolver;
import com.adrabazha.gypsy.board.utils.resolver.TaskHashResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TaskServiceImpl implements TaskService {

    private static final String INITIAL_TASK_DESCRIPTION = "{\"ops\":[{\"insert\":\"\\n\"}]}";

    private final BoardColumnHashResolver boardColumnHashResolver;
    private final TaskHashResolver taskHashResolver;
    private final BoardColumnService boardColumnService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final CustomEventPublisher eventPublisher;

    @Autowired
    public TaskServiceImpl(BoardColumnHashResolver boardColumnHashResolver,
                           TaskHashResolver taskHashResolver,
                           BoardColumnService boardColumnService,
                           TaskRepository taskRepository,
                           TaskMapper taskMapper,
                           UserService userService,
                           CustomEventPublisher eventPublisher) {
        this.boardColumnHashResolver = boardColumnHashResolver;
        this.taskHashResolver = taskHashResolver;
        this.boardColumnService = boardColumnService;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    private Task findById(Long taskId) {
        return taskRepository.
    }

    @Override
    public TaskResponse getTask(Long taskId) {
        Task task = findById(taskId);
        return taskMapper.mapTaskToResponse(task);
    }

    @Override
    public TaskReferenceResponse createTask(TaskCreateForm taskForm, User currentUser) {
        Long columnId = boardColumnHashResolver.retrieveIdentifier(taskForm.getColumnHash());
        BoardColumn boardColumn = boardColumnService.findById(columnId);
        Integer taskOrder = getNextPosition(boardColumn);

        Task task = Task.builder()
                .taskName(taskForm.getTaskName().trim())
                .taskDescription(INITIAL_TASK_DESCRIPTION)
                .boardColumn(boardColumn)
                .taskOrder(taskOrder)
                .build();
        Task persistedTask = taskRepository.save(task);

        eventPublisher.publishOrganizationRelatedEvent(this,
                boardColumn.getBoard().getOrganization(),
                currentUser,
                MessageTemplates.taskCreated(persistedTask));

        return taskMapper.mapTaskToReferenceResponse(persistedTask);
    }

    @Override
    public void updateTask(TaskUpdateForm taskUpdateForm, User currentUser) {
        Long taskId = taskHashResolver.retrieveIdentifier(taskUpdateForm.getTaskHash());

        Task task = findById(taskId);
        Task.TaskBuilder taskBuilder = task.toBuilder()
                .taskName(taskUpdateForm.getTaskName())
                .taskDescription(taskUpdateForm.getTaskDescription());

        if (Objects.nonNull(taskUpdateForm.getAssigneeUserName())) {
            User assignee = userService.findUserByUsername(taskUpdateForm.getAssigneeUserName());
            taskBuilder.userAssigned(assignee);
        }

        Task updatedTask = taskRepository.save(taskBuilder.build());

        eventPublisher.publishOrganizationRelatedEvent(this,
                updatedTask.getBoardColumn().getBoard().getOrganization(),
                currentUser,
                MessageTemplates.taskUpdated(updatedTask));
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
