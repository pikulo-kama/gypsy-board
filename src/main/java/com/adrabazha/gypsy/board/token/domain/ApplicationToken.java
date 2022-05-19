package com.adrabazha.gypsy.board.token.domain;

import java.util.Date;

public interface ApplicationToken {

    Date getExpiryDate();

    String getToken();
}
