package com.adrabazha.gypsy.board.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ColumnSynchronizationForm {

    private String sourceColumnHash;

    private String targetColumnHash;

    private List<String> taskHashList;
}
