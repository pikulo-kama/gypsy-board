package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.response.UserResponse;
import com.adrabazha.gypsy.board.service.OrganizationService;
import com.adrabazha.gypsy.board.service.SessionService;
import com.adrabazha.gypsy.board.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;
    private final OrganizationService organizationService;
    private final SessionService sessionService;

    @Autowired
    public UserRestController(UserService userService, OrganizationService organizationService,
                              SessionService sessionService) {
        this.userService = userService;
        this.organizationService = organizationService;
        this.sessionService = sessionService;
    }

    @GetMapping("/lookup")
    public List<UserResponse> getUsersByInput(@AuthenticationPrincipal User currentUser,
                                              @RequestParam("input") String input) {
        List<UserResponse> foundUsers = userService.lookupByInputString(input);

        return foundUsers.stream()
                .filter(user -> !StringUtils.equals(user.getUsername(), currentUser.getUsername()))
                .collect(Collectors.toList());
    }

    @GetMapping("/activeOrganizationMembers")
    public List<UserResponse> getActiveOrganizationMembers(HttpServletRequest request) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return organizationService.getOrganizationMembers(token.getOrganizationId());
    }
}
