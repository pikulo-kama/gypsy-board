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
@Table(name = Tables.BOARD_COLUMN)
public class BoardColumn {

    @Id
    @Column(name = PrimaryKeys.BOARD_COLUMN)
    @GeneratedValue
    private Long columnId;

    private String columnName;

    private Integer columnOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "boardColumn", fetch = FetchType.EAGER)
    private List<Task> tasks;
}
