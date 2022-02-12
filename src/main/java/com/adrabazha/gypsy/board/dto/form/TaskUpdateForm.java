package com.adrabazha.gypsy.board.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskUpdateForm {

    private String taskHash;

    private String taskName;

    private String taskDescription;

    private String assigneeUserName;
}
