package com.adrabazha.gypsy.board.mapper;

import com.adrabazha.gypsy.board.domain.nosql.OrganizationDocument;
import com.adrabazha.gypsy.board.dto.response.DocumentReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.DocumentResponse;
import com.adrabazha.gypsy.board.utils.resolver.DocumentHashResolver;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    private final DocumentHashResolver documentHashResolver;

    public DocumentMapper(DocumentHashResolver documentHashResolver) {
        this.documentHashResolver = documentHashResolver;
    }

    public DocumentReferenceResponse mapDocumentToReferenceResponse(OrganizationDocument document) {
        return DocumentReferenceResponse.builder()
                .documentHash(documentHashResolver.obtainHash(document.getDocumentId()))
                .documentHeader(document.getDocumentHeader())
                .build();
    }

    public DocumentResponse mapDocumentToResponse(OrganizationDocument document) {
        return DocumentResponse.builder()
                .documentHash(documentHashResolver.obtainHash(document.getDocumentId()))
                .documentData(document.getDocumentData())
                .documentHeader(document.getDocumentHeader())
                .build();
    }
}
