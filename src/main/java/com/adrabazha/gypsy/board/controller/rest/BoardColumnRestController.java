package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.annotation.OrganizationAccess;
import com.adrabazha.gypsy.board.domain.Role;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.ColumnCreateForm;
import com.adrabazha.gypsy.board.dto.form.ColumnUpdateForm;
import com.adrabazha.gypsy.board.service.BoardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.adrabazha.gypsy.board.domain.Role.ADMIN;
import static com.adrabazha.gypsy.board.domain.Role.ASSISTANT;

@RestController
@RequestMapping("/api/v1/columns")
public class BoardColumnRestController {

    private final BoardColumnService boardColumnService;

    @Autowired
    public BoardColumnRestController(BoardColumnService boardColumnService) {
        this.boardColumnService = boardColumnService;
    }

    @OrganizationAccess({ADMIN, ASSISTANT})
    @PostMapping("/create")
    public UserMessage createBoardColumn(@Validated @RequestBody ColumnCreateForm columnCreateForm,
                                         @AuthenticationPrincipal User currentUser) {
        return boardColumnService.createBoardColumn(columnCreateForm, currentUser);
    }

    @OrganizationAccess({ADMIN, ASSISTANT})
    @PostMapping("/update")
    public void updateColumnName(@Validated @RequestBody ColumnUpdateForm columnUpdateForm,
                                 @AuthenticationPrincipal User currentUser) {
        boardColumnService.updateColumnName(columnUpdateForm, currentUser);
    }

    @OrganizationAccess({ADMIN, ASSISTANT})
    @PostMapping("/delete")
    public UserMessage deleteBoardColumn(@RequestParam("c") String columnHash) {
        return boardColumnService.deleteBoardColumn(columnHash);
    }
}
