package com.adrabazha.gipsy.board.service;

import com.adrabazha.gipsy.board.domain.Task;
import com.adrabazha.gipsy.board.exception.GeneralException;
import com.adrabazha.gipsy.board.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Object form) {
        return null;
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new GeneralException("Task not found"));
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task updateById(Long id, Object form) {
        return null;
    }
}
