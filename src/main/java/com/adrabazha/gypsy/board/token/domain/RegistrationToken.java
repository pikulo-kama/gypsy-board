package com.adrabazha.gypsy.board.token.domain;

import com.adrabazha.gypsy.board.domain.PrimaryKeyConstant;
import com.adrabazha.gypsy.board.domain.DatabaseEntityConstant;
import com.adrabazha.gypsy.board.domain.sql.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DatabaseEntityConstant.REGISTRATION_TOKEN)
public class RegistrationToken implements ApplicationToken {

    @Id
    @Column(name = PrimaryKeyConstant.REGISTRATION_TOKEN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String token;

    @NotNull
    private Date expiryDate;

    @NotNull
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
