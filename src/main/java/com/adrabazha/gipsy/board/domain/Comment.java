package com.adrabazha.gipsy.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Tables.COMMENT)
public class Comment {

    @Id
    @Column(name = PrimaryKeys.COMMENT)
    @GeneratedValue
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private Long authorId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    private Boolean edited;
}
