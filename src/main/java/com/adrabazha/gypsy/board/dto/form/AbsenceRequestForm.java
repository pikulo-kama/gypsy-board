package com.adrabazha.gypsy.board.dto.form;

import com.adrabazha.gypsy.board.domain.AbsenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AbsenceRequestForm {

    @NotNull
    private Date startDate;

    @NotNull
    @Future
    private Date endDate;

    @NotNull
    private AbsenceType absenceType;
}
