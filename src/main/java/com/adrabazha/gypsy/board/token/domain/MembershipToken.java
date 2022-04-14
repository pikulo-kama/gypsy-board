package com.adrabazha.gypsy.board.token.domain;

import com.adrabazha.gypsy.board.domain.PrimaryKeys;
import com.adrabazha.gypsy.board.domain.Tables;
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
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.MEMBERSHIP_TOKEN)
public class MembershipToken implements Token {

    @Id
    @Column(name = PrimaryKeys.MEMBERSHIP_TOKEN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String token;

    @NotNull
    private Date expiryDate;

    private Long userId;

    private Long organizationId;
}
