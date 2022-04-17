package com.adrabazha.gypsy.board.domain.sql;

import com.adrabazha.gypsy.board.domain.PrimaryKeys;
import com.adrabazha.gypsy.board.domain.Tables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long organizationId;

    @NotNull
    private String organizationName;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_organization",
            joinColumns = @JoinColumn(name = "organization_id", referencedColumnName = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private List<User> members;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Board> boards;
}
