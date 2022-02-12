package com.adrabazha.gypsy.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardReferenceResponse {

    private String boardName;

    private String boardHash;
}
