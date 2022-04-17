package com.adrabazha.gypsy.board.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChartSectionDto {

    private String sectionName;

    @JsonProperty("y")
    private Double percentage;
}
