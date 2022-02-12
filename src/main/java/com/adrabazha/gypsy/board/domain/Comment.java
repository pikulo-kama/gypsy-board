package com.adrabazha.gypsy.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @NotNull
    private String body;

    @NotNull
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "task_id")
    private Task task;

    @NotNull
    private Long authorId;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean edited;
}
