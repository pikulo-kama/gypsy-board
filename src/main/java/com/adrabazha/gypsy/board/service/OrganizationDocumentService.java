package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.DocumentCreateForm;
import com.adrabazha.gypsy.board.dto.form.DocumentUpdateForm;

public interface OrganizationDocumentService {

    UserMessage getOrganizationRelatedDocuments(Long organizationId);

    UserMessage createDocument(DocumentCreateForm documentCreateForm, Long organizationId, User currentUser);

    UserMessage updateDocument(DocumentUpdateForm documentUpdateForm);

    UserMessage deleteDocument(String documentHash);

    UserMessage getSpecificDocument(String documentHash);
}
