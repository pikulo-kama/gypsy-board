package com.adrabazha.gypsy.board.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ColumnSynchronizationForm {

    @NotBlank
    private String sourceColumnHash;

    @NotBlank
    private String targetColumnHash;

    @NotNull
    private List<String> taskHashList;
}
