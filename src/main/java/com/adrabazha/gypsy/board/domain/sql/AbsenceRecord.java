package com.adrabazha.gypsy.board.domain.sql;

import com.adrabazha.gypsy.board.domain.AbsenceType;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = Tables.ABSENCE_RECORD)
public class AbsenceRecord {

    @Id
    @Column(name = PrimaryKeys.ABSENCE_RECORD)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long absenceId;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    private AbsenceType absenceType;

    private Boolean isCancelled;

    private Boolean isApproved;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "member_id")
    private User member;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public Boolean inPeriod(Date date) {
        return date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime();
    }
}
