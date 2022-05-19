package com.adrabazha.gypsy.board.domain.sql;

import com.adrabazha.gypsy.board.domain.PrimaryKeyConstant;
import com.adrabazha.gypsy.board.domain.DatabaseEntityConstant;
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
@Table(name = DatabaseEntityConstant.BOARD)
public class Board {

    @Id
    @Column(name = PrimaryKeyConstant.BOARD)
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
