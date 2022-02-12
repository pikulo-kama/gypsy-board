package com.adrabazha.gypsy.board.controller;

import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.form.OrganizationForm;
import com.adrabazha.gypsy.board.dto.response.OrganizationResponse;
import com.adrabazha.gypsy.board.service.OrganizationService;
import com.adrabazha.gypsy.board.service.SessionService;
import com.adrabazha.gypsy.board.utils.resolver.OrganizationHashResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationHashResolver organizationHashResolver;
    private final SessionService sessionService;

    @Autowired
    public OrganizationController(OrganizationService organizationService,
                                  OrganizationHashResolver organizationHashResolver, SessionService sessionService) {
        this.organizationService = organizationService;
        this.organizationHashResolver = organizationHashResolver;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String getOrganization(@AuthenticationPrincipal User currentUser,
                                  @RequestParam("o") String organizationHash,
                                  HttpServletRequest request,
                                  Model model) {
        Long organizationId = organizationHashResolver.retrieveIdentifier(organizationHash);
        OrganizationResponse organization = organizationService.getOrganizationResponseDto(organizationId, currentUser);
        organization.setOrganizationHash(organizationHash);
        sessionService.setUserActiveOrganization(organizationHash, organizationId, request);
        model.addAttribute("organization", organization);
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
        return "organization";
    }
}
