package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.sql.AbsenceRecord;
import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public interface AbsenceRecordRepository extends JpaRepository<AbsenceRecord, Long> {

    List<AbsenceRecord> findAllByOrganizationAndMember(@NotNull Organization organization, @NotNull User member);

    List<AbsenceRecord> findAllByOrganization(@NotNull Organization organization);

    List<AbsenceRecord> findAllByOrganizationAndStartDateAfterAndEndDateBefore(@NotNull Organization organization, @NotNull Date startDate, @NotNull Date endDate);
}