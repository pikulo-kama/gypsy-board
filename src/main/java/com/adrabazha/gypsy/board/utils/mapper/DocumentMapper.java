package com.adrabazha.gypsy.board.utils.mapper;

import com.adrabazha.gypsy.board.domain.nosql.OrganizationDocument;
import com.adrabazha.gypsy.board.dto.response.DocumentReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.DocumentResponse;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    private final HashResolverFactory hashResolverFactory;

    public DocumentMapper(HashResolverFactory hashResolverFactory) {
        this.hashResolverFactory = hashResolverFactory;
    }

    public DocumentReferenceResponse mapDocumentToReferenceResponse(OrganizationDocument document) {
        return DocumentReferenceResponse.builder()
                .documentHash(hashResolverFactory.obtainHash(document.getDocumentId(), Resolver.DOCUMENT))
                .documentHeader(document.getDocumentHeader())
                .build();
    }

    public DocumentResponse mapDocumentToResponse(OrganizationDocument document) {
        return DocumentResponse.builder()
                .documentHash(hashResolverFactory.obtainHash(document.getDocumentId(), Resolver.DOCUMENT))
                .documentData(document.getDocumentData())
                .documentHeader(document.getDocumentHeader())
                .build();
    }
}
