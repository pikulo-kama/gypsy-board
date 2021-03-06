package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.form.ColumnSynchronizationForm;
import com.adrabazha.gypsy.board.dto.form.TaskCreateForm;
import com.adrabazha.gypsy.board.dto.form.TaskUpdateForm;
import com.adrabazha.gypsy.board.dto.response.TaskReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.TaskResponse;
import com.adrabazha.gypsy.board.service.TaskService;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskRestController {

    private final TaskService taskService;
    private final HashResolverFactory hashResolverFactory;

    @Autowired
    public TaskRestController(TaskService taskService, HashResolverFactory hashResolverFactory) {
        this.taskService = taskService;
        this.hashResolverFactory = hashResolverFactory;
    }

    @GetMapping
    public TaskResponse getTask(@RequestParam("t") String taskHash) {
        Long taskId = hashResolverFactory.retrieveIdentifier(taskHash);
        return taskService.getTask(taskId);
    }

    @PostMapping("/delete")
    public void deleteTask(@RequestParam("t") String taskHash) {
        Long taskId = hashResolverFactory.retrieveIdentifier(taskHash);
        taskService.deleteTask(taskId);
    }

    @PostMapping("/create")
    public TaskReferenceResponse createTask(@Valid @RequestBody TaskCreateForm taskForm,
                                            @AuthenticationPrincipal User currentUser) {
        return taskService.createTask(taskForm, currentUser);
    }

    @PostMapping("/update")
    public void updateTask(@Valid @RequestBody TaskUpdateForm taskForm, @AuthenticationPrincipal User currentUser) {
        taskService.updateTask(taskForm, currentUser);
    }

    @PostMapping("/sync")
    public void synchronizeColumnTasks(@Valid @RequestBody ColumnSynchronizationForm columnSynchronizationForm) {
        taskService.synchronizeTasks(columnSynchronizationForm);
    }
}
