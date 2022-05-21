package com.adrabazha.gypsy.board.aop;

import com.adrabazha.gypsy.board.annotation.OrganizationAccess;
import com.adrabazha.gypsy.board.annotation.SharedOrganizationAccess;
import com.adrabazha.gypsy.board.domain.sql.Organization;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SharedOrganizationAccessAspect {

    public void validatedMemberRole(JoinPoint joinPoint, SharedOrganizationAccess access) {

    }
}
