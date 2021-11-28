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
@Table(name = Tables.BOARD)
public class Board {

    @Id
    @Column(name = PrimaryKeys.BOARD)
    @GeneratedValue
    private Long id;

    private String boardName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<BoardColumn> boardColumns;
}
