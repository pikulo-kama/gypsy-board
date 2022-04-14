package com.adrabazha.gypsy.board.controller;

import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.response.OrganizationReferenceResponse;
import com.adrabazha.gypsy.board.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class LandingController {

    private final OrganizationService organizationService;

    @Autowired
    public LandingController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public String getHomePage(@AuthenticationPrincipal User currentUser, Model model) {
        List<OrganizationReferenceResponse> availableOrganizations = organizationService.getOrganizationsByUser(currentUser.getUserId());
        model.addAttribute("availableOrganizations", availableOrganizations);
        return "index";
    }
}
