package com.adrabazha.gypsy.board.dto.response;

import com.adrabazha.gypsy.board.domain.sql.Organization;

import java.util.List;

public class OrganizationDocumentResponse {

    private Organization organization;

    private List<OrganizationDocumentDto> documents;
}
