package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.exception.GeneralException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    private static final String ORGANIZATION_TOKEN = "organizationToken";

    @Override
    public void setUserActiveOrganization(String organizationHash, Long organizationId, HttpServletRequest request) {
        OrganizationToken token = OrganizationToken.builder()
                .organizationId(organizationId)
                .organizationHash(organizationHash)
                .build();
        request.getSession().setAttribute(ORGANIZATION_TOKEN, token);
    }

    @Override
    public OrganizationToken getUserActiveOrganization(HttpServletRequest request) {
        OrganizationToken token = (OrganizationToken) request.getSession().getAttribute(ORGANIZATION_TOKEN);
        return Optional.ofNullable(token).orElseThrow(() -> new GeneralException("Token is not initialized"));
    }
}
