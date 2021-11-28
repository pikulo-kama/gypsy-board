package com.adrabazha.gipsy.board.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.USER)
public class User {

    @Id
    @Column(name = PrimaryKeys.USER)
    @GeneratedValue
    private Long userId;

    private String username;

    private String fullName;

    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_organization",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id", referencedColumnName = "organization_id"))
    private List<Organization> organizations;

    @Transient
    private Organization activeOrganization;
}


