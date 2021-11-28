package com.adrabazha.gipsy.board.service;

import com.adrabazha.gipsy.board.domain.Board;
import com.adrabazha.gipsy.board.domain.Organization;
import com.adrabazha.gipsy.board.domain.User;
import com.adrabazha.gipsy.board.service.crud.BaseService;

import java.util.List;

public interface BoardService extends BaseService<Board, Long, Object, Object> {
    List<Board> filterBoardsByName(User authUser, String boardName);
}
