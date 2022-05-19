package com.adrabazha.gypsy.board.token.service;

import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.token.context.TokenContext;
import com.adrabazha.gypsy.board.token.domain.ApplicationToken;

public interface TokenService<T extends ApplicationToken, C extends TokenContext> {

    T createToken(C context);

    Boolean validate(String token);

    void cleanExpiredTokens();
}
