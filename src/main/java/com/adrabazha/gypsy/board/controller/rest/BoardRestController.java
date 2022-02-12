package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.BoardCreateForm;
import com.adrabazha.gypsy.board.dto.form.BoardUpdateForm;
import com.adrabazha.gypsy.board.service.BoardService;
import com.adrabazha.gypsy.board.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardRestController {

    private final BoardService boardService;
    private final SessionService sessionService;

    @Autowired
    public BoardRestController(BoardService boardService, SessionService sessionService) {
        this.boardService = boardService;
        this.sessionService = sessionService;
    }

    @PostMapping("/create")
    public UserMessage createBoard(@Validated @RequestBody BoardCreateForm boardCreateForm, HttpServletRequest request) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return boardService.createBoard(boardCreateForm, token);
    }

    @PostMapping("/update")
    public UserMessage updateBoard(@Validated @RequestBody BoardUpdateForm boardUpdateForm) {
        return boardService.updateBoard(boardUpdateForm);
    }

    @PostMapping("/delete")
    public void deleteBoard(@RequestParam("b") String boardHash) {
        boardService.deleteBoard(boardHash);
    }
}
