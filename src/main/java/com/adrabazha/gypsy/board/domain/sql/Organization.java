package com.adrabazha.gypsy.board.domain.sql;

import com.adrabazha.gypsy.board.domain.DatabaseEntityConstant;
import com.adrabazha.gypsy.board.domain.PrimaryKeyConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.WhereJoinTable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DatabaseEntityConstant.ORGANIZATION)
public class Organization {

    @Id
    @Column(name = PrimaryKeyConstant.ORGANIZATION)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long organizationId;

    @NotNull
    private String organizationName;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_organization",
            joinColumns = @JoinColumn(name = "organization_id", referencedColumnName = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    @WhereJoinTable(clause = "is_invitation_accepted = true")
    private List<User> activeMembers;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_organization",
            joinColumns = @JoinColumn(name = "organization_id", referencedColumnName = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private List<User> allMembers;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Board> boards;
}
