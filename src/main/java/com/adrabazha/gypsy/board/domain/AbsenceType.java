package com.adrabazha.gypsy.board.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AbsenceType {
    SICKNESS("Sickness", "<i class=\"fas fa-head-side-virus\"></i>"),
    VACATION("Vacation", "<i class=\"fas fa-plane-departure\"></i>");

    @Getter
    private String representation;

    @Getter
    private String icon;
}
