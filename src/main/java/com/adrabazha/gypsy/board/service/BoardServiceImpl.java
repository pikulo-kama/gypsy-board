package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.Board;
import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.BoardCreateForm;
import com.adrabazha.gypsy.board.dto.form.BoardUpdateForm;
import com.adrabazha.gypsy.board.dto.response.BoardReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.BoardResponse;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.exception.UserMessageException;
import com.adrabazha.gypsy.board.mapper.BoardMapper;
import com.adrabazha.gypsy.board.repository.BoardRepository;
import com.adrabazha.gypsy.board.utils.mail.CustomEventPublisher;
import com.adrabazha.gypsy.board.utils.mail.templates.MessageTemplates;
import com.adrabazha.gypsy.board.utils.resolver.BoardHashResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardHashResolver boardHashResolver;
    private final BoardRepository boardRepository;
    private final OrganizationService organizationService;
    private final BoardMapper boardMapper;
    private final CustomEventPublisher eventPublisher;

    @Autowired
    public BoardServiceImpl(BoardHashResolver boardHashResolver,
                            BoardRepository boardRepository,
                            OrganizationService organizationService,
                            BoardMapper boardMapper,
                            CustomEventPublisher eventPublisher) {
        this.boardHashResolver = boardHashResolver;
        this.boardRepository = boardRepository;
        this.organizationService = organizationService;
        this.boardMapper = boardMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException("Board not found"));
    }

    @Override
    public BoardResponse getBoardResponse(String boardHash, User currentUser) {
        Long boardId = boardHashResolver.retrieveIdentifier(boardHash);
        Board board = getBoardIfUserHaveAccess(boardId, currentUser)
                .orElseThrow(() -> new UserMessageException("User doesn't have access to the board"));

        return boardMapper.mapBoardToResponse(board).toBuilder()
                .boardHash(boardHash)
                .build();
    }

    @Override
    public UserMessage createBoard(BoardCreateForm dto, OrganizationToken token, User currentUser) {
        String boardName = dto.getBoardName();
        if (boardRepository.existsByBoardName(boardName)) {
            throw new UserMessageException(String.format("Board with name '%s' already exists.", boardName));
        }

        Organization organization = organizationService.findById(token.getOrganizationId());

        Board board = Board.builder()
                .boardName(boardName)
                .organization(organization)
                .build();

        Board persistedBoard = boardRepository.save(board);

        eventPublisher.publishOrganizationRelatedEvent(this,
                organization,
                currentUser,
                MessageTemplates.boardCreated(persistedBoard));

        String boardHash = boardHashResolver.obtainHash(persistedBoard.getId());
        UserMessage response = UserMessage.success(String.format("Board '%s' was successfully created!", boardName));
        response.addResponseDataEntry("persistedBoard", BoardReferenceResponse.builder()
                .boardHash(boardHash)
                .boardName(boardName)
                .build());

        return response;
    }

    @Override
    public UserMessage updateBoard(BoardUpdateForm dto, User currentUser) {
        Long boardId = boardHashResolver.retrieveIdentifier(dto.getBoardHash());
        Board board = findById(boardId);
        board.setBoardName(dto.getBoardName());
        Board updatedBoard = boardRepository.save(board);

        eventPublisher.publishOrganizationRelatedEvent(this,
                board.getOrganization(),
                currentUser,
                MessageTemplates.boardUpdated(updatedBoard));

        return UserMessage.success("Board name was updated");
    }

    @Override
    public UserMessage deleteBoard(String boardHash) {
        Long boardId = boardHashResolver.retrieveIdentifier(boardHash);
        boardRepository.deleteById(boardId);

        return UserMessage.success("Board successfully deleted");
    }

    private Optional<Board> getBoardIfUserHaveAccess(Long boardId, User currentUser) {
        Optional<Board> boardOptional = Optional.empty();
        Board board = findById(boardId);
        if (organizationService.isUserInOrganization(currentUser, board.getOrganization())) {
            boardOptional = Optional.of(board);
        }
        return boardOptional;
    }
}
