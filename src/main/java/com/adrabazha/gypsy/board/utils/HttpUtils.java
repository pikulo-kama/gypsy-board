package com.adrabazha.gypsy.board.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {
    public static String getPreviousPageUrl(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.REFERER);
    }

    public static String getBaseUrlFromRequest(HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
    }
}
