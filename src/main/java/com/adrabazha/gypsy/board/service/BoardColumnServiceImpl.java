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
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
public class BoardColumnServiceImpl implements BoardColumnService {

    private final HashResolverFactory hashResolverFactory;
    private final BoardService boardService;
    private final BoardColumnRepository boardColumnRepository;
    private final CustomEventPublisher eventPublisher;

    @Autowired
    public BoardColumnServiceImpl(HashResolverFactory hashResolverFactory,
                                  BoardService boardService,
                                  BoardColumnRepository boardColumnRepository,
                                  CustomEventPublisher eventPublisher) {
        this.hashResolverFactory = hashResolverFactory;
        this.boardService = boardService;
        this.boardColumnRepository = boardColumnRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public BoardColumn findById(Long boardColumnId) {
        return boardColumnRepository.findById(boardColumnId)
                .orElseThrow(() -> new GeneralException("Board column not found"));
    }

    @Override
    public void updateColumnName(ColumnUpdateForm columnUpdateForm, User currentUser) {
        Long columnId = hashResolverFactory.retrieveIdentifier(columnUpdateForm.getColumnHash());
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
        Long boardId = hashResolverFactory.retrieveIdentifier(columnCreateForm.getBoardHash());
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
                .columnHash(hashResolverFactory.obtainHash(persistedBoardColumn.getColumnId(), Resolver.BOARD_COLUMN))
                .columnName(persistedBoardColumn.getColumnName())
                .build());
        return userMessage;
    }

    @Override
    public UserMessage deleteBoardColumn(String columnHash) {
        Long columnId = hashResolverFactory.retrieveIdentifier(columnHash);
        boardColumnRepository.deleteById(columnId);
        return UserMessage.success("Column was removed");
    }

    @Override
    public BoardColumn save(BoardColumn boardColumn) {
        return boardColumnRepository.save(boardColumn);
    }
}
