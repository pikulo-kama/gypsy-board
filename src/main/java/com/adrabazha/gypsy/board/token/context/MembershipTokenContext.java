package com.adrabazha.gypsy.board.token.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MembershipTokenContext implements TokenContext {

    private Long organizationId;

    private Long userId;
}
