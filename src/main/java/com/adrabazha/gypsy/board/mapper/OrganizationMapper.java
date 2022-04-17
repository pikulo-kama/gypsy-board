package com.adrabazha.gypsy.board.mapper;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.dto.response.BoardReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.OrganizationReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.OrganizationResponse;
import com.adrabazha.gypsy.board.dto.response.UserReferenceResponse;
import com.adrabazha.gypsy.board.utils.resolver.OrganizationHashResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrganizationMapper {

    private final BoardMapper boardMapper;
    private final UserMapper userMapper;
    private final OrganizationHashResolver organizationHashResolver;

    @Autowired
    public OrganizationMapper(BoardMapper boardMapper, UserMapper userMapper,
                              OrganizationHashResolver organizationHashResolver) {
        this.boardMapper = boardMapper;
        this.userMapper = userMapper;
        this.organizationHashResolver = organizationHashResolver;
    }

    public OrganizationResponse mapOrganizationToResponse(Organization organization) {
        List<UserReferenceResponse> members = organization.getMembers().stream()
                .map(userMapper::mapUserToReferenceResponse)
                .collect(Collectors.toList());

        List<BoardReferenceResponse> boards = organization.getBoards().stream()
                .map(boardMapper::mapBoardToReferenceResponse)
                .collect(Collectors.toList());

        return OrganizationResponse.builder()
                .organizationName(organization.getOrganizationName())
                .organizationMembers(members)
                .organizationBoards(boards)
                .build();
    }

    public OrganizationReferenceResponse mapOrganizationsToReferenceResponse(Organization organization) {
        return OrganizationReferenceResponse.builder()
                .organizationName(organization.getOrganizationName())
                .organizationHash(organizationHashResolver.obtainHash(organization.getOrganizationId()))
                .build();
    }
}
