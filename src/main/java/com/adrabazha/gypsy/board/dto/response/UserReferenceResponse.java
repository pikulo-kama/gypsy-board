package com.adrabazha.gypsy.board.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonSerialize
public class UserReferenceResponse {

    private String userHash;

    private Boolean isInvitationAccepted;

    private String fullName;

    private String activeRole;
}
