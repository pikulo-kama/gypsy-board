package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.Board;
import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.BoardCreateForm;
import com.adrabazha.gypsy.board.dto.form.BoardUpdateForm;
import com.adrabazha.gypsy.board.dto.form.SharedBoardCreateForm;
import com.adrabazha.gypsy.board.dto.response.BoardReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.BoardResponse;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.exception.UserMessageException;
import com.adrabazha.gypsy.board.utils.mapper.BoardMapper;
import com.adrabazha.gypsy.board.repository.BoardRepository;
import com.adrabazha.gypsy.board.utils.mail.CustomEventPublisher;
import com.adrabazha.gypsy.board.utils.mail.templates.MessageTemplates;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    private final HashResolverFactory hashResolverFactory;
    private final BoardRepository boardRepository;
    private final OrganizationService organizationService;
    private final BoardMapper boardMapper;
    private final CustomEventPublisher eventPublisher;

    @Autowired
    public BoardServiceImpl(HashResolverFactory hashResolverFactory,
                            BoardRepository boardRepository,
                            OrganizationService organizationService,
                            BoardMapper boardMapper,
                            CustomEventPublisher eventPublisher) {
        this.hashResolverFactory = hashResolverFactory;
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
    public BoardResponse getBoardResponse(String boardHash, User currentUser, Long organizationId) {
        Long boardId = hashResolverFactory.retrieveIdentifier(boardHash);
        Board board = getBoardIfUserHaveAccess(boardId, organizationId, currentUser)
                .orElseThrow(() -> new UserMessageException("User doesn't have access to the board"));

        return boardMapper.mapBoardToResponse(board).toBuilder()
                .boardHash(boardHash)
                .build();
    }

    @Override
    public UserMessage createBoard(BoardCreateForm dto, Long organizationId, User currentUser) {
        Board persistedBoard = doCreateBoard(dto.getBoardName(), organizationId, currentUser);

        String boardHash = hashResolverFactory.obtainHash(persistedBoard.getId(), Resolver.BOARD);
        UserMessage response = UserMessage.success(String.format("Board '%s' was successfully created!",
                persistedBoard.getBoardName()));
        response.addResponseDataEntry("persistedBoard", BoardReferenceResponse.builder()
                .boardHash(boardHash)
                .boardName(persistedBoard.getBoardName())
                .build());

        return response;
    }

    @Override
    public UserMessage updateBoard(BoardUpdateForm dto, User currentUser) {
        Long boardId = hashResolverFactory.retrieveIdentifier(dto.getBoardHash());
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
        Long boardId = hashResolverFactory.retrieveIdentifier(boardHash);
        boardRepository.deleteById(boardId);

        return UserMessage.success("Board successfully deleted");
    }

    @Override
    @Transactional
    public UserMessage createSharedBoard(SharedBoardCreateForm sharedBoardCreateForm, Long organizationId, User currentUser) {

        List<Long> collaboratorsIds = sharedBoardCreateForm.getCollaboratorList().stream()
                .map(hashResolverFactory::retrieveIdentifier)
                .collect(Collectors.toList());

        collaboratorsIds.forEach(collaboratorId -> {
            if (organizationId.equals(collaboratorId)) {
                throw new UserMessageException("You can't make yourself as collaborator");
            }
        });

        Board createdBoard = doCreateBoard(sharedBoardCreateForm.getBoardName(), organizationId, currentUser);

        collaboratorsIds.forEach(collaboratorOrganizationId ->
                boardRepository.shareBoard(createdBoard.getId(), collaboratorOrganizationId));

        String boardHash = hashResolverFactory.obtainHash(createdBoard.getId(), Resolver.BOARD);

        UserMessage response = UserMessage.success("Shared board was successfully created");
        response.addResponseDataEntry("persistedBoard", BoardReferenceResponse.builder()
                .boardHash(boardHash)
                .boardName(createdBoard.getBoardName())
                .build());


        return response;
    }

    private Board doCreateBoard(String boardName, Long organizationId, User currentUser) {
        Organization organization = organizationService.findById(organizationId);

        if (boardRepository.existsByBoardNameAndOrganization(boardName, organization)) {
            throw new UserMessageException(String.format("Board with name '%s' already exists.", boardName));
        }

        Board board = Board.builder()
                .boardName(boardName)
                .organization(organization)
                .build();

        Board persistedBoard = boardRepository.save(board);

        eventPublisher.publishOrganizationRelatedEvent(this,
                organization,
                currentUser,
                MessageTemplates.boardCreated(persistedBoard));

        return persistedBoard;
    }

    private Optional<Board> getBoardIfUserHaveAccess(Long boardId,
                                                     Long organizationId,
                                                     User currentUser) {
        Optional<Board> boardOptional = Optional.empty();
        Board board = findById(boardId);
        if (organizationService.isUserInOrganization(currentUser, board.getOrganization()) ||
                isBoardSharedWithOrganization(board.getId(), organizationId)
        ) {
            boardOptional = Optional.of(board);
        }
        return boardOptional;
    }

    private Boolean isBoardSharedWithOrganization(Long boardId, Long organizationId) {
        List<Long> rawCollaborators = boardRepository.getCollaboratorsIdsByBoard(boardId);
        return rawCollaborators.stream()
                .anyMatch(collaboratorId -> collaboratorId.equals(organizationId));
    }
}
