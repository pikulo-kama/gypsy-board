package com.adrabazha.gypsy.board.controller;

import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.response.BoardResponse;
import com.adrabazha.gypsy.board.service.BoardService;
import com.adrabazha.gypsy.board.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final SessionService sessionService;

    @Autowired
    public BoardController(BoardService boardService,
                           SessionService sessionService) {
        this.boardService = boardService;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String getBoard(@RequestParam("b") String boardHash,
                           @AuthenticationPrincipal User currentUser,
                           HttpServletRequest request,
                           Model model) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        BoardResponse board = boardService.getBoardResponse(boardHash, currentUser, token.getOrganizationId());
        model.addAttribute("board", board);
        return "board";
    }
}
