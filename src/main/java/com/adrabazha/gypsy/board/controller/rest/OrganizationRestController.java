package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.OrganizationForm;
import com.adrabazha.gypsy.board.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationRestController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationRestController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/create")
    public UserMessage createOrganization(@AuthenticationPrincipal User currentUser,
                                          @Validated @RequestBody OrganizationForm organizationForm) {
        return organizationService.createOrganization(organizationForm, currentUser);
    }

    @PostMapping("/delete")
    public UserMessage deleteOrganization(@RequestParam("o") String organizationHash) {
        return organizationService.deleteOrganization(organizationHash);
    }
}
