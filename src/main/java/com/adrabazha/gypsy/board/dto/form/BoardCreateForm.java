package com.adrabazha.gypsy.board.dto.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonSerialize
public class BoardCreateForm {

    @NotBlank
    private String boardName;
}
