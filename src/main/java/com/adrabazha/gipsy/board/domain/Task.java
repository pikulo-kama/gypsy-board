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
@Table(name = Tables.TASK)
public class Task {

    @Id
    @Column(name = PrimaryKeys.TASK)
    @GeneratedValue
    private Long taskId;

    private String taskName;

    @Column
    private Long userAssignedId;

    private Integer taskOrder;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private BoardColumn boardColumn;

    private String taskDescription;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<Comment> comments;
}
