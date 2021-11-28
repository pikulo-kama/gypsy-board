package com.adrabazha.gipsy.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.ORGANIZATION)
public class Organization {

    @Id
    @Column(name = PrimaryKeys.ORGANIZATION)
    @GeneratedValue
    private Long organizationId;

    private String organizationName;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_organization",
            joinColumns = @JoinColumn(name = "organization_id", referencedColumnName = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private List<User> users;
}
