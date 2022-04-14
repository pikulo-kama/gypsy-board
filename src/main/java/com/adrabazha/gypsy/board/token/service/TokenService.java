package com.adrabazha.gypsy.board.token.service;

import com.adrabazha.gypsy.board.token.domain.Token;
import com.adrabazha.gypsy.board.token.context.TokenContext;

import java.util.Calendar;
import java.util.Date;

public interface TokenService<T extends Token, C extends TokenContext> {

    T createToken(C context);

    void cleanExpiredTokens();

    Boolean validate(String token);

    default Date getExpiryDate(Integer lifetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, lifetime);

        return calendar.getTime();
    }

    default Boolean isExpired(Token token) {
        long expiryDate = token.getExpiryDate().getTime();
        long now = new Date().getTime();
        return now > expiryDate;
    }
}
