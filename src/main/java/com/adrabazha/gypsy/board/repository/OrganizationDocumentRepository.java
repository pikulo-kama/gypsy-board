package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.nosql.OrganizationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrganizationDocumentRepository extends MongoRepository<OrganizationDocument, Long> {

    List<OrganizationDocument> findAllByOrganizationId(Long organizationId);
}
