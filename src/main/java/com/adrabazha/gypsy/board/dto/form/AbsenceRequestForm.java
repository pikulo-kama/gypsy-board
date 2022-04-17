package com.adrabazha.gypsy.board.dto.form;

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
public class AbsenceRequestForm {

    private Date startDate;

    private Date endDate;

    private AbsenceType absenceType;
}
