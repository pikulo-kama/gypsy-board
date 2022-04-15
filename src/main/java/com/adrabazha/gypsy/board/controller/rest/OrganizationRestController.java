package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.annotation.OrganizationAccess;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.OrganizationForm;
import com.adrabazha.gypsy.board.dto.form.OrganizationMemberForm;
import com.adrabazha.gypsy.board.dto.form.OrganizationMembersForm;
import com.adrabazha.gypsy.board.dto.form.UpdateMemberRoleForm;
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
                                          @Validated @RequestBody OrganizationForm organizationForm,
                                          HttpServletRequest request) {
        return organizationService.createOrganization(organizationForm, currentUser, request);
    }

    @OrganizationAccess({ADMIN})
    @PostMapping("/delete")
    public UserMessage deleteOrganization(@RequestParam("o") String organizationHash) {
        return organizationService.deleteOrganization(organizationHash);
    }

    @GetMapping("/restrictionSelector")
    public UserMessage getMemberBlockedActionsSelector(@AuthenticationPrincipal User currentUser,
                                                       HttpServletRequest request) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return organizationService.getMemberBlockedActionsSelector(currentUser.getUserId(), token.getOrganizationId());
    }

    @OrganizationAccess({ADMIN})
    @PostMapping("/updateUserRole")
    public UserMessage updateMemberRole(@Validated @RequestBody UpdateMemberRoleForm form, HttpServletRequest request) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return organizationService.updateMemberRole(form, token.getOrganizationId());
    }

    @OrganizationAccess({ADMIN, ASSISTANT})
    @PostMapping("/addMembers")
    public UserMessage addOrganizationMembers(@Validated @RequestBody OrganizationMembersForm form,
                                              HttpServletRequest request) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return organizationService.addMembersToOrganization(form, token.getOrganizationId(), request);
    }

    @OrganizationAccess({ADMIN})
    @PostMapping("/removeMember")
    public UserMessage removeOrganizationMember(@Validated @RequestBody OrganizationMemberForm form,
                                                HttpServletRequest request) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return organizationService.removeOrganizationMember(form, token.getOrganizationId());
    }
}
