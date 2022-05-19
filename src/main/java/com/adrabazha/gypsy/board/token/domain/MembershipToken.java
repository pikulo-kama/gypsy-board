package com.adrabazha.gypsy.board.token.domain;

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
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DatabaseEntityConstant.MEMBERSHIP_TOKEN)
public class MembershipToken implements ApplicationToken {

    @Id
    @Column(name = PrimaryKeyConstant.MEMBERSHIP_TOKEN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String token;

    @NotNull
    private Date expiryDate;

    private Long userId;

    private Long organizationId;
}
