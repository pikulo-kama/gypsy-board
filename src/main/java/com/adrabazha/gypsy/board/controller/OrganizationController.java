package com.adrabazha.gypsy.board.controller;

import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.response.OrganizationResponse;
import com.adrabazha.gypsy.board.service.OrganizationService;
import com.adrabazha.gypsy.board.service.SessionService;
import com.adrabazha.gypsy.board.token.service.MembershipTokenService;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final HashResolverFactory hashResolverFactory;
    private final SessionService sessionService;
    private final MembershipTokenService membershipTokenService;

    @Autowired
    public OrganizationController(OrganizationService organizationService,
                                  HashResolverFactory hashResolverFactory,
                                  SessionService sessionService,
                                  MembershipTokenService membershipTokenService) {
        this.organizationService = organizationService;
        this.hashResolverFactory = hashResolverFactory;
        this.sessionService = sessionService;
        this.membershipTokenService = membershipTokenService;
    }

    @GetMapping
    public String getOrganization(@AuthenticationPrincipal User currentUser,
                                  @RequestParam("o") String organizationHash,
                                  HttpServletRequest request,
                                  Model model) {
        Long organizationId = hashResolverFactory.retrieveIdentifier(organizationHash);
        OrganizationResponse organization = organizationService.getOrganizationResponseDto(organizationId, currentUser);
        organization.setOrganizationHash(organizationHash);
        sessionService.setUserActiveOrganization(organizationHash, organizationId, request);
        model.addAttribute("organization", organization);
        model.addAttribute("organizationRoles", organizationService.getOrganizationRoles());
        return "organization";
    }

    @GetMapping("/active")
    public String getActiveOrganization(@AuthenticationPrincipal User currentUser,
                                        HttpServletRequest request,
                                        Model model) {
        OrganizationToken organizationToken = sessionService.getUserActiveOrganization(request);
        OrganizationResponse organization = organizationService.getOrganizationResponseDto(organizationToken.getOrganizationId(), currentUser);
        organization.setOrganizationHash(organizationToken.getOrganizationHash());
        model.addAttribute("organization", organization);
        model.addAttribute("organizationRoles", organizationService.getOrganizationRoles());
        return "organization";
    }

    @GetMapping("/acceptInvitation")
    public String acceptInvitation(@RequestParam("token") String token) {
        Boolean isValid = membershipTokenService.validate(token);
        return "redirect:" + (isValid ? "/login" : "/error");
    }
}
