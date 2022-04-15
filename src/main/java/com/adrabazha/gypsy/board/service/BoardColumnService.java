package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.BoardColumn;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.ColumnCreateForm;
import com.adrabazha.gypsy.board.dto.form.ColumnUpdateForm;

public interface BoardColumnService {

    BoardColumn findById(Long boardColumnId);

    void updateColumnName(ColumnUpdateForm columnUpdateForm);

    UserMessage createBoardColumn(ColumnCreateForm columnCreateForm);

    UserMessage deleteBoardColumn(String columnHash);

    BoardColumn save(BoardColumn boardColumn);
}
