package com.adrabazha.gypsy.board.utils;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {
    public static String getPreviousPageUrl(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.REFERER);
    }
}
