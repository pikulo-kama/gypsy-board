package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.Board;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.BoardCreateForm;
import com.adrabazha.gypsy.board.dto.form.BoardUpdateForm;
import com.adrabazha.gypsy.board.dto.response.BoardResponse;

public interface BoardService {

    Board findById(Long boardId);

    BoardResponse getBoardResponse(String boardHash, User currentUser);

    UserMessage createBoard(BoardCreateForm dto, OrganizationToken token, User currentUser);

    UserMessage updateBoard(BoardUpdateForm dto, User currentUser);

    UserMessage deleteBoard(String boardHash);
}
