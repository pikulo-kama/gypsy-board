package com.adrabazha.gypsy.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum UserMessageType {

    SUCCESS("success", "#4E9F3D", "#1E5128"),
    ERROR("error", "#F90716", "#FFCA03");

    @Getter
    private String type;

    @Getter
    private String primaryColor;

    @Getter
    private String secondaryColor;
}
