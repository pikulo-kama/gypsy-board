package com.adrabazha.gypsy.board.dto.response;

import com.adrabazha.gypsy.board.domain.AbsenceType;
import com.adrabazha.gypsy.board.domain.sql.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AbsenceRequestResponse {

    private String absenceRecordHash;

    private User organizationMember;

    private Date startDate;

    private Date endDate;

    private Integer absenceDays;

    private AbsenceType absenceType;
}
