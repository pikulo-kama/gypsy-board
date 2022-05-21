package com.adrabazha.gypsy.board.utils.mapper;

import com.adrabazha.gypsy.board.domain.sql.AbsenceRecord;
import com.adrabazha.gypsy.board.dto.response.AbsenceRecordResponse;
import com.adrabazha.gypsy.board.dto.response.AbsenceRequestResponse;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AbsenceRecordMapper {

    private final HashResolverFactory hashResolverFactory;

    public AbsenceRecordMapper(HashResolverFactory hashResolverFactory) {
        this.hashResolverFactory = hashResolverFactory;
    }

    public AbsenceRecordResponse mapToAbsenceResponse(AbsenceRecord absenceRecord) {
        return AbsenceRecordResponse.builder()
                .absenceRecordHash(hashResolverFactory.obtainHash(absenceRecord.getAbsenceId(), Resolver.ABSENCE_RECORD))
                .absenceDays(getAbsenceDayPeriod(absenceRecord))
                .absenceType(absenceRecord.getAbsenceType())
                .startDate(absenceRecord.getStartDate())
                .endDate(absenceRecord.getEndDate())
                .isApproved(absenceRecord.getIsApproved())
                .isCancelled(absenceRecord.getIsCancelled())
                .build();
    }

    public AbsenceRequestResponse mapToAbsenceRequest(AbsenceRecord absenceRecord) {
        return AbsenceRequestResponse.builder()
                .absenceRecordHash(hashResolverFactory.obtainHash(absenceRecord.getAbsenceId(), Resolver.ABSENCE_RECORD))
                .absenceType(absenceRecord.getAbsenceType())
                .absenceDays(getAbsenceDayPeriod(absenceRecord))
                .startDate(absenceRecord.getStartDate())
                .endDate(absenceRecord.getEndDate())
                .organizationMember(absenceRecord.getMember())
                .build();
    }

    private Integer getAbsenceDayPeriod(AbsenceRecord absenceRecord) {
        long duration = absenceRecord.getEndDate().getTime() - absenceRecord.getStartDate().getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(duration) + 1;
    }
}
