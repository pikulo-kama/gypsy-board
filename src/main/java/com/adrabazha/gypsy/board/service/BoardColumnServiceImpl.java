package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.Board;
import com.adrabazha.gypsy.board.domain.sql.BoardColumn;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.ColumnCreateForm;
import com.adrabazha.gypsy.board.dto.form.ColumnUpdateForm;
import com.adrabazha.gypsy.board.dto.response.BoardColumnReferenceResponse;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.repository.BoardColumnRepository;
import com.adrabazha.gypsy.board.utils.mail.CustomEventPublisher;
import com.adrabazha.gypsy.board.utils.mail.templates.MessageTemplates;
import com.adrabazha.gypsy.board.utils.resolver.BoardColumnHashResolver;
import com.adrabazha.gypsy.board.utils.resolver.BoardHashResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
public class BoardColumnServiceImpl implements BoardColumnService {

    private final BoardService boardService;
    private final BoardHashResolver boardHashResolver;
    private final BoardColumnRepository boardColumnRepository;
    private final BoardColumnHashResolver boardColumnHashResolver;
    private final CustomEventPublisher eventPublisher;

    @Autowired
    public BoardColumnServiceImpl(BoardService boardService,
                                  BoardHashResolver boardHashResolver,
                                  BoardColumnRepository boardColumnRepository,
                                  BoardColumnHashResolver boardColumnHashResolver,
                                  CustomEventPublisher eventPublisher) {
        this.boardService = boardService;
        this.boardHashResolver = boardHashResolver;
        this.boardColumnRepository = boardColumnRepository;
        this.boardColumnHashResolver = boardColumnHashResolver;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public BoardColumn findById(Long boardColumnId) {
        return boardColumnRepository.findById(boardColumnId)
                .orElseThrow(() -> new GeneralException("Board column not found"));
    }

    @Override
    public void updateColumnName(ColumnUpdateForm columnUpdateForm, User currentUser) {
        Long columnId = boardColumnHashResolver.retrieveIdentifier(columnUpdateForm.getColumnHash());
        BoardColumn boardColumn = findById(columnId);
        boardColumn.setColumnName(columnUpdateForm.getColumnName());
        BoardColumn updatedColumn = boardColumnRepository.save(boardColumn);

        eventPublisher.publishOrganizationRelatedEvent(this,
                updatedColumn.getBoard().getOrganization(),
                currentUser,
                MessageTemplates.columnUpdated(updatedColumn));
    }

    @Override
    public UserMessage createBoardColumn(ColumnCreateForm columnCreateForm, User currentUser) {
        Long boardId = boardHashResolver.retrieveIdentifier(columnCreateForm.getBoardHash());
        Board board = boardService.findById(boardId);
        Optional<BoardColumn> lastBoardColumn = board.getBoardColumns().stream()
                .max(Comparator.comparingInt(BoardColumn::getColumnOrder));
        int nextColumnPosition = 1;

        if (lastBoardColumn.isPresent()) {
            nextColumnPosition = lastBoardColumn.get().getColumnOrder() + 1;
        }

        BoardColumn boardColumn = BoardColumn.builder()
                .columnName(columnCreateForm.getColumnName())
                .board(board)
                .columnOrder(nextColumnPosition)
                .build();
        BoardColumn persistedBoardColumn = boardColumnRepository.save(boardColumn);

        eventPublisher.publishOrganizationRelatedEvent(this,
                persistedBoardColumn.getBoard().getOrganization(),
                currentUser,
                MessageTemplates.columnCreated(persistedBoardColumn));

        UserMessage userMessage = UserMessage.success("Column was added");
        userMessage.addResponseDataEntry("boardColumn", BoardColumnReferenceResponse.builder()
                .columnHash(boardColumnHashResolver.obtainHash(persistedBoardColumn.getColumnId()))
                .columnName(persistedBoardColumn.getColumnName())
                .build());
        return userMessage;
    }

    @Override
    public UserMessage deleteBoardColumn(String columnHash) {
        Long columnId = boardColumnHashResolver.retrieveIdentifier(columnHash);
        boardColumnRepository.deleteById(columnId);
        return UserMessage.success("Column was removed");
    }

    @Override
    public BoardColumn save(BoardColumn boardColumn) {
        return boardColumnRepository.save(boardColumn);
    }
}
