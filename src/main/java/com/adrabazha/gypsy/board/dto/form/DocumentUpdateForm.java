package com.adrabazha.gypsy.board.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentUpdateForm {

    @NotBlank
    private String documentHash;

    @NotNull
    private String documentHeader;

    @NotNull
    private String documentData;
}
