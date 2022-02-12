package com.adrabazha.gypsy.board.mapper;

import com.adrabazha.gypsy.board.domain.Board;
import com.adrabazha.gypsy.board.domain.BoardColumn;
import com.adrabazha.gypsy.board.dto.response.BoardColumnResponse;
import com.adrabazha.gypsy.board.dto.response.BoardReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.BoardResponse;
import com.adrabazha.gypsy.board.utils.resolver.BoardHashResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoardMapper {

    private final BoardHashResolver boardHashResolver;
    private final BoardColumnMapper boardColumnMapper;

    @Autowired
    public BoardMapper(BoardHashResolver boardHashResolver, BoardColumnMapper boardColumnMapper) {
        this.boardHashResolver = boardHashResolver;
        this.boardColumnMapper = boardColumnMapper;
    }

    public BoardResponse mapBoardToResponse(Board board) {
        List<BoardColumnResponse> boardColumnResponseList = board.getBoardColumns().stream()
                .sorted(Comparator.comparingInt(BoardColumn::getColumnOrder))
                .map(boardColumnMapper::mapBoardColumnToResponse)
                .collect(Collectors.toList());

        return BoardResponse.builder()
                .boardName(board.getBoardName())
                .boardColumns(boardColumnResponseList)
                .build();
    }

    public BoardReferenceResponse mapBoardToReferenceResponse(Board board) {
        return BoardReferenceResponse.builder()
                .boardName(board.getBoardName())
                .boardHash(boardHashResolver.obtainHash(board.getId()))
                .build();
    }
}
