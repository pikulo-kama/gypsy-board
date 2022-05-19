package com.adrabazha.gypsy.board.domain.sql;

import com.adrabazha.gypsy.board.domain.PrimaryKeyConstant;
import com.adrabazha.gypsy.board.domain.DatabaseEntityConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DatabaseEntityConstant.ORGANIZATION_ROLE)
public class OrganizationRole {

    @Id
    @Column(name = PrimaryKeyConstant.ORGANIZATION_ROLE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String roleCode;

    private String roleName;
}
