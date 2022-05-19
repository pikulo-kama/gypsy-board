package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.form.ColumnSynchronizationForm;
import com.adrabazha.gypsy.board.dto.form.TaskCreateForm;
import com.adrabazha.gypsy.board.dto.form.TaskUpdateForm;
import com.adrabazha.gypsy.board.dto.response.TaskReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.TaskResponse;

public interface TaskService {

    TaskResponse getTask(Long taskId);

    TaskReferenceResponse createTask(TaskCreateForm taskCreateForm, User currentUser);

    void updateTask(TaskUpdateForm taskUpdateForm, User currentUser);

    void synchronizeTasks(ColumnSynchronizationForm columnSynchronizationForm);

    void deleteTask(Long taskId);
}
