package com.adrabazha.gypsy.board.controller;

import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.service.OrganizationDocumentService;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import com.adrabazha.gypsy.board.utils.resolver.impl.OrganizationHashResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    private final OrganizationDocumentService organizationDocumentService;
    private final HashResolverFactory hashResolverFactory;

    public DocumentController(OrganizationDocumentService organizationDocumentService,
                              HashResolverFactory hashResolverFactory) {
        this.organizationDocumentService = organizationDocumentService;
        this.hashResolverFactory = hashResolverFactory;
    }

    @GetMapping
    public String getDocumentsPage(@RequestParam("o") String organizationHash, Model model) {
        Long organizationId = hashResolverFactory.retrieveIdentifier(organizationHash);
        UserMessage message = organizationDocumentService.getOrganizationRelatedDocuments(organizationId);

        model.addAllAttributes(message.getResponseData());
        return "document";
    }
}
