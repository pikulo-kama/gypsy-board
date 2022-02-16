package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.annotation.OrganizationAccess;
import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.OrganizationForm;
import com.adrabazha.gypsy.board.service.OrganizationService;
import com.adrabazha.gypsy.board.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.adrabazha.gypsy.board.domain.Role.ADMIN;
import static com.adrabazha.gypsy.board.domain.Role.ASSISTANT;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationRestController {

    private final OrganizationService organizationService;
    private final SessionService sessionService;

    @Autowired
    public OrganizationRestController(OrganizationService organizationService, SessionService sessionService) {
        this.organizationService = organizationService;
        this.sessionService = sessionService;
    }

    @PostMapping("/create")
    public UserMessage createOrganization(@AuthenticationPrincipal User currentUser,
                                          @Validated @RequestBody OrganizationForm organizationForm) {
        return organizationService.createOrganization(organizationForm, currentUser);
    }

    @OrganizationAccess({ADMIN})
    @PostMapping("/delete")
    public UserMessage deleteOrganization(@RequestParam("o") String organizationHash,
                                          @AuthenticationPrincipal User currentUser) {
        return organizationService.deleteOrganization(organizationHash, currentUser);
    }

    @GetMapping("/restrictionSelector")
    public UserMessage getMemberBlockedActionsSelector(@AuthenticationPrincipal User currentUser,
                                                       HttpServletRequest request) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return organizationService.getMemberBlockedActionsSelector(currentUser.getUserId(), token.getOrganizationId());
    }
}
