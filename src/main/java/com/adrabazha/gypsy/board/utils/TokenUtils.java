package com.adrabazha.gypsy.board.utils;

import com.adrabazha.gypsy.board.token.domain.ApplicationToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {

    public static Date getExpiryDate(Integer lifetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, lifetime);

        return calendar.getTime();
    }

    public static Boolean isExpired(ApplicationToken applicationToken) {
        long expiryDate = applicationToken.getExpiryDate().getTime();
        long now = new Date().getTime();
        return now > expiryDate;
    }
}
