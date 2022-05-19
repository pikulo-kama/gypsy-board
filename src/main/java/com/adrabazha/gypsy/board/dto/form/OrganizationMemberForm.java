package com.adrabazha.gypsy.board.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrganizationMemberForm {

    @NotBlank
    private String memberHash;
}
