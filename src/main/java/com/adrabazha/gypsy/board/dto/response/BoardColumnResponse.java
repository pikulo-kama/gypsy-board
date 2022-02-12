package com.adrabazha.gypsy.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BoardColumnResponse {

    private String columnName;

    private String columnHash;

    private Integer columnOrder;

    private List<TaskReferenceResponse> tasks;
}
