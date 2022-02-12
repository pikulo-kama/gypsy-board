package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.dto.OrganizationToken;

import javax.servlet.http.HttpServletRequest;

public interface SessionService {

    String ORGANIZATION_TOKEN = "organizationToken";

    void setUserActiveOrganization(String organizationHash, Long organizationId, HttpServletRequest request);

    OrganizationToken getUserActiveOrganization(HttpServletRequest request);
}
