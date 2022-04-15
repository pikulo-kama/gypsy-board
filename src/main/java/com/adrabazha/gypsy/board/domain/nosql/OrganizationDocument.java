package com.adrabazha.gypsy.board.domain.nosql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@Document(collection = "documents")
public class OrganizationDocument {

    @Id
    private Long documentId;

    private Long organizationId;

    private String documentHeader;

    private String documentData;

    private Long authorId;
}
