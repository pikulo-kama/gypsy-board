package com.adrabazha.gypsy.board.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskUpdateForm {

    @NotBlank
    private String taskHash;

    @NotBlank
    private String taskName;

    @NotBlank
    private String taskDescription;

    private String assigneeUserName;
}
