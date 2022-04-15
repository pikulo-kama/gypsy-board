package com.adrabazha.gypsy.board.domain.sql;

import com.adrabazha.gypsy.board.domain.Comment;
import com.adrabazha.gypsy.board.domain.PrimaryKeys;
import com.adrabazha.gypsy.board.domain.Tables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.TASK)
public class Task {

    @Id
    @Column(name = PrimaryKeys.TASK)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @NotNull
    private String taskName;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "user_assigned_id")
    private User userAssigned;

    private Integer taskOrder;

    @NotNull
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "column_id")
    private BoardColumn boardColumn;

    private String taskDescription;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Comment> comments;
}
