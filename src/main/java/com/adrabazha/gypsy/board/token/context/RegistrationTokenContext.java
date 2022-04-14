package com.adrabazha.gypsy.board.token.context;

import com.adrabazha.gypsy.board.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RegistrationTokenContext implements TokenContext {

    private User user;
}
