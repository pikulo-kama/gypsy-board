package com.adrabazha.gypsy.board.controller.rest;

import com.adrabazha.gypsy.board.annotation.OrganizationAccess;
import com.adrabazha.gypsy.board.domain.Role;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.OrganizationToken;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.AbsenceRequestForm;
import com.adrabazha.gypsy.board.dto.form.CapacityChartForm;
import com.adrabazha.gypsy.board.service.AbsenceService;
import com.adrabazha.gypsy.board.service.SessionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/absences")
public class AbsenceRestController {

    private final AbsenceService absenceService;
    private final SessionService sessionService;

    public AbsenceRestController(AbsenceService absenceService,
                                 SessionService sessionService) {
        this.absenceService = absenceService;
        this.sessionService = sessionService;
    }

    @PostMapping("/request")
    public UserMessage requestAbsence(@Validated @RequestBody AbsenceRequestForm form,
                                      HttpServletRequest request,
                                      @AuthenticationPrincipal User currentUser) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return absenceService.createAbsenceRequest(form, token.getOrganizationId(), currentUser);
    }

    @OrganizationAccess({Role.ADMIN})
    @PostMapping("/approve")
    public UserMessage approveRequest(@RequestParam("abs") String absenceRecordHash,
                                      HttpServletRequest request) {
        return absenceService.approveAbsenceRequest(absenceRecordHash);
    }

    @PostMapping("/cancel")
    public UserMessage cancelAbsenceRequest(@RequestParam("abs") String absenceHash,
                                            HttpServletRequest request,
                                            @AuthenticationPrincipal User currentUser) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return absenceService.cancelAbsenceRequest(absenceHash, currentUser, token.getOrganizationId());
    }

    @PostMapping("/chart")
    public UserMessage generateCapacityChart(@Validated @RequestBody CapacityChartForm form, HttpServletRequest request) {
        OrganizationToken token = sessionService.getUserActiveOrganization(request);
        return absenceService.generateCapacityChart(form, token.getOrganizationId());
    }
}
