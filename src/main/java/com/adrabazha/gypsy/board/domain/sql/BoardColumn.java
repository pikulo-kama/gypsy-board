package com.adrabazha.gypsy.board.domain.sql;

import com.adrabazha.gypsy.board.domain.PrimaryKeys;
import com.adrabazha.gypsy.board.domain.Tables;
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
@Table(name = Tables.BOARD_COLUMN)
public class BoardColumn {

    @Id
    @Column(name = PrimaryKeys.BOARD_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long columnId;

    @NotNull
    private String columnName;

    @NotNull
    private Integer columnOrder;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "boardColumn", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Task> tasks;
}
