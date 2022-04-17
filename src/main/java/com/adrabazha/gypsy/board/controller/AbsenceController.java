package com.adrabazha.gypsy.board.controller;

import com.adrabazha.gypsy.board.annotation.OrganizationAccess;
import com.adrabazha.gypsy.board.domain.Role;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.service.AbsenceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/absences")
public class AbsenceController {

    private final AbsenceService absenceService;

    public AbsenceController(AbsenceService absenceService) {
        this.absenceService = absenceService;
    }

    @GetMapping
    public String getAbsenceHistory(@RequestParam("o") String organizationHash,
                                    @AuthenticationPrincipal User currentUser,
                                    Model model) {
        UserMessage userMessage = absenceService.getMemberAbsenceHistory(organizationHash, currentUser);
        model.addAllAttributes(userMessage.getResponseData());

        return "absence";
    }

    @GetMapping("/manage")
    @OrganizationAccess({Role.ADMIN})
    public String getAbsenceManagementScreen(@RequestParam("o") String organizationHash,
                                             @AuthenticationPrincipal User currentUser,
                                             Model model) {
        UserMessage userMessage = absenceService.getOrganizationAbsenceRequests(organizationHash);
        model.addAllAttributes(userMessage.getResponseData());

        return "absence_manage";
    }

    @GetMapping("/chart")
    public String getOrganizationCapacityChart(@RequestParam("o") String organizationHash) {
        return "chart";
    }
}
