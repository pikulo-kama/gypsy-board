package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.nosql.DatabaseSequence;
import com.adrabazha.gypsy.board.domain.nosql.OrganizationDocument;
import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.DocumentCreateForm;
import com.adrabazha.gypsy.board.dto.form.DocumentUpdateForm;
import com.adrabazha.gypsy.board.dto.response.DocumentReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.DocumentResponse;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.utils.mapper.DocumentMapper;
import com.adrabazha.gypsy.board.repository.OrganizationDocumentRepository;
import com.adrabazha.gypsy.board.utils.mail.CustomEventPublisher;
import com.adrabazha.gypsy.board.utils.mail.templates.MessageTemplates;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class OrganizationDocumentServiceImpl implements OrganizationDocumentService {

    private static final String DOCUMENT_SEQUENCE_NAME = "document_sequence";

    private final OrganizationDocumentRepository organizationDocumentRepository;
    private final HashResolverFactory hashResolverFactory;
    private final DocumentMapper documentMapper;
    private final UserService userService;
    private final OrganizationService organizationService;
    private final MongoOperations mongoOperations;
    private final CustomEventPublisher eventPublisher;

    public OrganizationDocumentServiceImpl(OrganizationDocumentRepository organizationDocumentRepository,
                                           HashResolverFactory hashResolverFactory,
                                           DocumentMapper documentMapper,
                                           UserService userService,
                                           OrganizationService organizationService,
                                           MongoOperations mongoOperations,
                                           CustomEventPublisher eventPublisher) {
        this.organizationDocumentRepository = organizationDocumentRepository;
        this.hashResolverFactory = hashResolverFactory;
        this.documentMapper = documentMapper;
        this.userService = userService;
        this.organizationService = organizationService;
        this.mongoOperations = mongoOperations;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserMessage getOrganizationRelatedDocuments(Long organizationId) {
        List<OrganizationDocument> documents = organizationDocumentRepository.findAllByOrganizationId(organizationId);

        Organization organization = organizationService.findById(organizationId);
        Map<Long, String> memberMap = userService.findUsersFromOrganization(organization)
                .stream().collect(Collectors.toMap(User::getUserId, User::getFullName));

        List<DocumentReferenceResponse> documentReferenceResponseList = documents.stream()
                .map(document -> {
                    DocumentReferenceResponse reference = documentMapper.mapDocumentToReferenceResponse(document);
                    reference.setAuthorName(memberMap.get(document.getAuthorId()));
                    return reference;
                })
                .collect(Collectors.toList());

        UserMessage userMessage = UserMessage.success("Documents were successfully retrieved");
        userMessage.addResponseDataEntry("documents", documentReferenceResponseList);

        return userMessage;
    }

    @Override
    public UserMessage createDocument(DocumentCreateForm documentCreateForm, Long organizationId, User currentUser) {
        OrganizationDocument document = OrganizationDocument.builder()
                .documentId(generateDocumentId())
                .documentHeader(documentCreateForm.getDocumentHeader())
                .documentData(documentCreateForm.getDocumentData())
                .authorId(currentUser.getUserId())
                .organizationId(organizationId)
                .build();

        OrganizationDocument persistedDocument = organizationDocumentRepository.save(document);

        DocumentReferenceResponse createdDocument = documentMapper.mapDocumentToReferenceResponse(persistedDocument);
        createdDocument.setAuthorName(currentUser.getFullName());
        
        eventPublisher.publishOrganizationRelatedEvent(this, 
                organizationService.findById(organizationId), 
                currentUser, 
                MessageTemplates.documentWasCreated(persistedDocument));
        
        UserMessage userMessage = UserMessage.success("Document was created");
        userMessage.addResponseDataEntry("createdDocument", createdDocument);

        return userMessage;
    }

    @Override
    public UserMessage updateDocument(DocumentUpdateForm documentUpdateForm, User currentUser) {
        Long documentId = hashResolverFactory.retrieveIdentifier(documentUpdateForm.getDocumentHash());
        OrganizationDocument document = findById(documentId);

        OrganizationDocument updatedDocument = document.toBuilder()
                .documentHeader(documentUpdateForm.getDocumentHeader())
                .documentData(documentUpdateForm.getDocumentData())
                .build();

        OrganizationDocument persistedDocument = organizationDocumentRepository.save(updatedDocument);
        DocumentReferenceResponse documentResponse = documentMapper.mapDocumentToReferenceResponse(persistedDocument);

        eventPublisher.publishOrganizationRelatedEvent(this,
                organizationService.findById(persistedDocument.getOrganizationId()),
                currentUser,
                MessageTemplates.documentWasModified(persistedDocument));

        UserMessage userMessage = UserMessage.success("Document was updated");
        userMessage.addResponseDataEntry("updatedDocument", documentResponse);

        return userMessage;
    }

    @Override
    public UserMessage deleteDocument(String documentHash, User currentUser) {
        Long documentId = hashResolverFactory.retrieveIdentifier(documentHash);
        OrganizationDocument document = findById(documentId);
        Organization organization = organizationService.findById(document.getOrganizationId());

        organizationDocumentRepository.deleteById(document.getDocumentId());

        eventPublisher.publishOrganizationRelatedEvent(this,
                organization,
                currentUser,
                MessageTemplates.documentWasDeleted(document));

        UserMessage userMessage = UserMessage.success("Document was removed");
        userMessage.addResponseDataEntry("removedDocumentHash", documentHash);

        return userMessage;
    }

    @Override
    public UserMessage getSpecificDocument(String documentHash) {
        Long documentId = hashResolverFactory.retrieveIdentifier(documentHash);
        OrganizationDocument document = findById(documentId);
        User author = userService.findById(document.getAuthorId());

        DocumentResponse documentResponse = documentMapper.mapDocumentToResponse(document);
        documentResponse.setAuthorName(author.getFullName());

        UserMessage userMessage = UserMessage.success("Document was retrieved");
        userMessage.addResponseDataEntry("document", documentResponse);

        return userMessage;
    }


    private OrganizationDocument findById(Long documentId) {
        return organizationDocumentRepository.findById(documentId)
                .orElseThrow(() -> new GeneralException("Document doesn't exist"));
    }

    private Long generateDocumentId() {
        DatabaseSequence counter = mongoOperations.findAndModify(
                query(where("_id").is(DOCUMENT_SEQUENCE_NAME)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
