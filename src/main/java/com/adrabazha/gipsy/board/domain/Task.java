package com.adrabazha.gipsy.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String taskName;

    private Long userAssignedId;

    private Integer taskOrder;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private Column column;

    private String taskDescription;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;
}
