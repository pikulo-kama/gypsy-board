package com.adrabazha.gypsy.board.dto.response;

import com.adrabazha.gypsy.board.domain.AbsenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AbsenceRecordResponse {

    private String absenceRecordHash;

    private Date startDate;

    private Date endDate;

    private Integer absenceDays;

    private Boolean isApproved;

    private Boolean isCancelled;

    private AbsenceType absenceType;
}
