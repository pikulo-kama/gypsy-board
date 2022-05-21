package com.adrabazha.gypsy.board.dto.response;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardReferenceResponse {

    private String boardName;

    private String boardHash;

    private Boolean isShared;

    private Organization ownerOrganization;
}
