package com.adrabazha.gypsy.board.aop;

import com.adrabazha.gypsy.board.annotation.OrganizationAccess;
import com.adrabazha.gypsy.board.domain.Role;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.exception.UserMessageException;
import com.adrabazha.gypsy.board.service.OrganizationService;
import com.adrabazha.gypsy.board.service.SessionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Aspect
public class OrganizationAccessAspect {

    private final HttpServletRequest request;
    private final SessionService sessionService;
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationAccessAspect(SessionService sessionService, HttpServletRequest request, OrganizationService organizationService) {
        this.sessionService = sessionService;
        this.request = request;
        this.organizationService = organizationService;
    }

    @Before("@annotation(access)")
    public void validateOrganizationMemberRole(JoinPoint joinPoint, OrganizationAccess access) {
        List<String> allowedRoles = Arrays.stream(access.value())
                                        .map(Role::getRoleName)
                                        .collect(Collectors.toList());

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OrganizationToken token = sessionService.getUserActiveOrganization(request);

        String userRole = organizationService.getOrganizationMemberRole(currentUser.getUserId(), token.getOrganizationId());

        if (!allowedRoles.contains(userRole)) {
            throw new UserMessageException("You do not have permissions to perform this action");
        }
    }
}
