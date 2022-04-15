package com.adrabazha.gypsy.board.dto.response;

import com.adrabazha.gypsy.board.domain.sql.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrganizationDocumentDto {

    private String documentHash;

    private String documentData;

    private User author;
}
