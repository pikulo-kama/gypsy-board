package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.dto.form.ColumnSynchronizationForm;
import com.adrabazha.gypsy.board.dto.form.TaskCreateForm;
import com.adrabazha.gypsy.board.dto.form.TaskUpdateForm;
import com.adrabazha.gypsy.board.dto.response.TaskReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.TaskResponse;
import com.adrabazha.gypsy.board.service.TaskService;
import com.adrabazha.gypsy.board.utils.resolver.TaskHashResolver;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final TaskHashResolver taskHashResolver;

    @Autowired
    public TaskRestController(TaskService taskService, TaskHashResolver taskHashResolver) {
        this.taskService = taskService;
        this.taskHashResolver = taskHashResolver;
    }

    @GetMapping
    public TaskResponse getTask(@RequestParam("t") String taskHash) {
        Long taskId = taskHashResolver.retrieveIdentifier(taskHash);
        return taskService.getTask(taskId);
    }

    @PostMapping("/delete")
    public void deleteTask(@RequestParam("t") String taskHash) {
        Long taskId = taskHashResolver.retrieveIdentifier(taskHash);
        taskService.deleteTask(taskId);
    }

    @PostMapping("/create")
    public TaskReferenceResponse createTask(@Valid @RequestBody TaskCreateForm taskForm) {
        return taskService.createTask(taskForm);
    }

    @PostMapping("/update")
    public void updateTask(@Valid @RequestBody TaskUpdateForm taskForm) {
        taskService.updateTask(taskForm);
    }

    @PostMapping("/sync")
    public void synchronizeColumnTasks(@Valid @RequestBody ColumnSynchronizationForm columnSynchronizationForm) {
        taskService.synchronizeTasks(columnSynchronizationForm);
    }
}
