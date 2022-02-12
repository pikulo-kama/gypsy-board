package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.ColumnCreateForm;
import com.adrabazha.gypsy.board.dto.form.ColumnUpdateForm;
import com.adrabazha.gypsy.board.service.BoardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/columns")
public class BoardColumnRestController {

    private final BoardColumnService boardColumnService;

    @Autowired
    public BoardColumnRestController(BoardColumnService boardColumnService) {
        this.boardColumnService = boardColumnService;
    }

    @PostMapping("/create")
    public UserMessage createBoardColumn(@Validated @RequestBody ColumnCreateForm columnCreateForm) {
        return boardColumnService.createBoardColumn(columnCreateForm);
    }

    @PostMapping("/update")
    public void updateColumnName(@Validated @RequestBody ColumnUpdateForm columnUpdateForm) {
        boardColumnService.updateColumnName(columnUpdateForm);
    }

    @PostMapping("/delete")
    public UserMessage deleteBoardColumn(@RequestParam("c") String columnHash) {
        return boardColumnService.deleteBoardColumn(columnHash);
    }
}
