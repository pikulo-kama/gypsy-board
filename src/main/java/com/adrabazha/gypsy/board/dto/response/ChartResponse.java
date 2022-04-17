package com.adrabazha.gypsy.board.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChartResponse {

    @Setter(AccessLevel.NONE)
    private List<ChartSectionDto> sections;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private Map<String, Double> sectionsRaw = new HashMap<>();

    public void increaseSectionShare(String sectionName) {
        increaseSectionShare(sectionName, 1d);
    }

    public void increaseSectionShare(String sectionName, Double increaseValue) {
        double currentShare = sectionsRaw.getOrDefault(sectionName, 0d);
        sectionsRaw.put(sectionName, currentShare + increaseValue);
    }

    public ChartResponse buildResponse() {
        double totalShares = sectionsRaw.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        this.sections = sectionsRaw.entrySet()
                .stream()
                .map(section -> new ChartSectionDto(section.getKey(), (section.getValue() / totalShares) * 100))
                .collect(Collectors.toList());

        return this;
    }
}
