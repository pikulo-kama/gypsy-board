package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.annotation.OrganizationAccess;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.BoardCreateForm;
import com.adrabazha.gypsy.board.dto.form.BoardUpdateForm;
import com.adrabazha.gypsy.board.service.BoardService;
import com.adrabazha.gypsy.board.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.adrabazha.gypsy.board.domain.Role.ADMIN;
import static com.adrabazha.gypsy.board.domain.Role.ASSISTANT;

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

    @OrganizationAccess({ADMIN, ASSISTANT})
    @PostMapping("/create")
    public UserMessage createBoard(@Validated @RequestBody BoardCreateForm boardCreateForm,
                                   HttpServletRequest request,
                                   @AuthenticationPrincipal User currentUser) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return boardService.createBoard(boardCreateForm, token, currentUser);
    }

    @OrganizationAccess({ADMIN, ASSISTANT})
    @PostMapping("/update")
    public UserMessage updateBoard(@Validated @RequestBody BoardUpdateForm boardUpdateForm,
                                   @AuthenticationPrincipal User currentUser) {
        return boardService.updateBoard(boardUpdateForm, currentUser);
    }

    @OrganizationAccess({ADMIN, ASSISTANT})
    @PostMapping("/delete")
    public UserMessage deleteBoard(@RequestParam("b") String boardHash) {
        return boardService.deleteBoard(boardHash);
    }
}
