package com.adrabazha.gypsy.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentResponse {

    private String documentHash;

    private String documentHeader;

    private String documentData;

    private String authorName;
}
