package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.AbsenceRequestForm;
import com.adrabazha.gypsy.board.dto.form.CapacityChartForm;

public interface AbsenceService {

    UserMessage createAbsenceRequest(AbsenceRequestForm form, Long organizationId, User user);

    UserMessage getMemberAbsenceHistory(String organizationHash, User user);

    UserMessage cancelAbsenceRequest(String absenceRecordHash, User currentUser, Long organizationId);

    UserMessage getOrganizationAbsenceRequests(String organizationHash);

    UserMessage approveAbsenceRequest(String absenceRecordHash);

    UserMessage generateCapacityChart(CapacityChartForm capacityChartForm, Long organizationId);
}
