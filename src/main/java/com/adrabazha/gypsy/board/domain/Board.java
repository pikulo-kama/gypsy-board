package com.adrabazha.gypsy.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.BOARD)
public class Board {

    @Id
    @Column(name = PrimaryKeys.BOARD)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String boardName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<BoardColumn> boardColumns;
}
