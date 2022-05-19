package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.DocumentCreateForm;
import com.adrabazha.gypsy.board.dto.form.DocumentUpdateForm;
import com.adrabazha.gypsy.board.service.OrganizationDocumentService;
import com.adrabazha.gypsy.board.service.SessionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentRestController {

    private final OrganizationDocumentService organizationDocumentService;
    private final SessionService sessionService;

    public DocumentRestController(OrganizationDocumentService organizationDocumentService,
                                  SessionService sessionService) {
        this.organizationDocumentService = organizationDocumentService;
        this.sessionService = sessionService;
    }

    @GetMapping
    public UserMessage getOrganizationDocuments(@RequestParam(value = "doc") String documentHash) {
        return organizationDocumentService.getSpecificDocument(documentHash);
    }

    @PostMapping("/create")
    public UserMessage createOrganizationDocument(@AuthenticationPrincipal User currentUser,
                                                  @Validated @RequestBody DocumentCreateForm documentCreateForm,
                                                  HttpServletRequest request) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return organizationDocumentService.createDocument(documentCreateForm, token.getOrganizationId(), currentUser);
    }

    @PostMapping("/update")
    public UserMessage updateOrganizationDocument(@Validated @RequestBody DocumentUpdateForm documentUpdateForm,
                                                  @AuthenticationPrincipal User currentUser) {
        return organizationDocumentService.updateDocument(documentUpdateForm, currentUser);
    }

    @PostMapping("/delete")
    public UserMessage deleteOrganizationDocument(@RequestParam("doc") String documentHash,
                                                  @AuthenticationPrincipal User currentUser) {
        return organizationDocumentService.deleteDocument(documentHash, currentUser);
    }
}
