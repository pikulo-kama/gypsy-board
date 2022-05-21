package com.adrabazha.gypsy.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrganizationResponse {

    private String organizationName;

    private String organizationHash;

    private List<UserReferenceResponse> organizationMembers;

    private List<BoardReferenceResponse> organizationBoards;

    public void addOrganizationBoards(List<BoardReferenceResponse> boardReferences) {
        organizationBoards.addAll(boardReferences);
    }
}
