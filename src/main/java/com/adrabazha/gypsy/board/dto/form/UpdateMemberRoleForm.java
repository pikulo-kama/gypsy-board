package com.adrabazha.gypsy.board.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateMemberRoleForm {

    @NotBlank
    private String roleCode;

    @NotBlank
    private String userHash;
}
