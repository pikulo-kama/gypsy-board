package com.adrabazha.gypsy.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrganizationToken implements Serializable {

    private Long organizationId;

    private String organizationHash;
}
