package com.adrabazha.gypsy.board.dto.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonSerialize
public class SharedBoardCreateForm {

    private String boardName;

    private List<String> collaboratorList;
}
