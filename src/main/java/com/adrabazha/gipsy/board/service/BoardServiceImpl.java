package com.adrabazha.gipsy.board.service;

import com.adrabazha.gipsy.board.domain.Board;
import com.adrabazha.gipsy.board.domain.User;
import com.adrabazha.gipsy.board.exception.GeneralException;
import com.adrabazha.gipsy.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private BoardRepository boardRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board create(Object form) {
        return null;
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Override
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Board doesn't exist"));
    }

    @Override
    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    public Board updateById(Long id, Object form) {
        return null;
    }

    @Override
    public List<Board> filterBoardsByName(User authUser, String boardName) {
        return boardRepository.findBoardsByOrganizationAndBoardNameContains(
                authUser.getActiveOrganization(), boardName);
    }
}
