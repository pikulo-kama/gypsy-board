package com.adrabazha.gypsy.board.service.auth;

import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.domain.auth.RegistrationToken;

import java.util.List;

public interface RegistrationTokenService {

    RegistrationToken createToken(User user);

    Boolean validateToken(String token);

    List<RegistrationToken> getExpiredTokens();

    void deleteExpiredTokens();
}
