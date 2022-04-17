package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.AbsenceRecord;
import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.AbsenceRequestForm;
import com.adrabazha.gypsy.board.dto.form.CapacityChartForm;
import com.adrabazha.gypsy.board.dto.response.AbsenceRecordResponse;
import com.adrabazha.gypsy.board.dto.response.AbsenceRequestResponse;
import com.adrabazha.gypsy.board.dto.response.ChartResponse;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.exception.UserMessageException;
import com.adrabazha.gypsy.board.mapper.AbsenceRecordMapper;
import com.adrabazha.gypsy.board.repository.AbsenceRecordRepository;
import com.adrabazha.gypsy.board.utils.resolver.AbsenceRecordHashResolver;
import com.adrabazha.gypsy.board.utils.resolver.OrganizationHashResolver;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final OrganizationService organizationService;
    private final OrganizationHashResolver organizationHashResolver;
    private final AbsenceRecordRepository absenceRecordRepository;
    private final AbsenceRecordMapper absenceRecordMapper;
    private final AbsenceRecordHashResolver absenceRecordHashResolver;

    public AbsenceServiceImpl(OrganizationService organizationService,
                              OrganizationHashResolver organizationHashResolver,
                              AbsenceRecordRepository absenceRecordRepository,
                              AbsenceRecordMapper absenceRecordMapper,
                              AbsenceRecordHashResolver absenceRecordHashResolver) {
        this.organizationService = organizationService;
        this.organizationHashResolver = organizationHashResolver;
        this.absenceRecordRepository = absenceRecordRepository;
        this.absenceRecordMapper = absenceRecordMapper;
        this.absenceRecordHashResolver = absenceRecordHashResolver;
    }

    @Override
    public UserMessage createAbsenceRequest(AbsenceRequestForm form, Long organizationId, User user) {
        Organization organization = organizationService.findById(organizationId);

        if (isRequestFormInvalid(form)) {
            throw new UserMessageException("Invalid absence period specified");
        }

        if (isAbsenceAlreadyRequested(form, organization, user)) {
            throw new UserMessageException("You already have absence requested on this date");
        }

        AbsenceRecord absenceRecord = AbsenceRecord.builder()
                .startDate(form.getStartDate())
                .endDate(form.getEndDate())
                .absenceType(form.getAbsenceType())
                .isCancelled(false)
                .isApproved(false)
                .member(user)
                .organization(organization)
                .build();

        AbsenceRecord createdRecord = absenceRecordRepository.save(absenceRecord);

        UserMessage userMessage = UserMessage.success("Absence was requested");
        userMessage.addResponseDataEntry("createdRecord", absenceRecordMapper.mapToAbsenceResponse(createdRecord));

        return userMessage;
    }

    @Override
    public UserMessage getMemberAbsenceHistory(String organizationHash, User member) {
        Long organizationId = organizationHashResolver.retrieveIdentifier(organizationHash);
        Organization organization = organizationService.findById(organizationId);
        List<AbsenceRecord> absences = absenceRecordRepository.findAllByOrganizationAndMember(organization, member);

        List<AbsenceRecordResponse> absencesResponse = absences.stream()
                .map(absenceRecordMapper::mapToAbsenceResponse)
                .sorted((f, s) -> Long.compare(s.getStartDate().getTime(), f.getStartDate().getTime()))
                .collect(Collectors.toList());

        UserMessage userMessage = UserMessage.success("Absence history was retrieved");
        userMessage.addResponseDataEntry("absenceRecords", absencesResponse);

        return userMessage;
    }

    @Override
    public UserMessage cancelAbsenceRequest(String absenceRecordHash, User currentUser, Long organizationId) {
        Long absenceRecordId = absenceRecordHashResolver.retrieveIdentifier(absenceRecordHash);

        AbsenceRecord absenceRecord = findById(absenceRecordId);
        absenceRecord.setIsCancelled(true);

        validate(absenceRecord, currentUser, organizationId);

        absenceRecordRepository.save(absenceRecord);
        return UserMessage.success("Request was successfully canceled");
    }

    @Override
    public UserMessage getOrganizationAbsenceRequests(String organizationHash) {
        Long organizationId = organizationHashResolver.retrieveIdentifier(organizationHash);
        Organization organization = organizationService.findById(organizationId);

        List<AbsenceRecord> records = absenceRecordRepository.findAllByOrganization(organization);

        List<AbsenceRequestResponse> absenceRequests = records.stream()
                .filter(this::validForReview)
                .map(absenceRecordMapper::mapToAbsenceRequest)
                .collect(Collectors.toList());

        UserMessage userMessage = UserMessage.success("Request retrieved");
        userMessage.addResponseDataEntry("requests", absenceRequests);

        return userMessage;
    }

    @Override
    public UserMessage approveAbsenceRequest(String absenceRecordHash) {
        Long absenceRecordId = absenceRecordHashResolver.retrieveIdentifier(absenceRecordHash);
        AbsenceRecord absenceRecord = findById(absenceRecordId);
        absenceRecord.setIsApproved(true);
        absenceRecordRepository.save(absenceRecord);

        return UserMessage.success("Absence request was successfully approved");
    }

    @Override
    public UserMessage generateCapacityChart(CapacityChartForm form, Long organizationId) {
        Organization organization = organizationService.findById(organizationId);
        List<AbsenceRecord> records = getAbsenceRecordsForPeriod(form.getStartDate(), form.getEndDate(), organization);
        records.removeIf(absenceRecord -> !absenceRecord.getIsApproved() || absenceRecord.getIsCancelled());

        ChartResponse chart = new ChartResponse();

        int membersSize = organization.getMembers().size();

        for (long time = form.getStartDate().getTime(); time <= form.getEndDate().getTime(); time += getOneDayMillis()) {

            Date currentDate = new Date(time);

            if (isWeekend(currentDate)) {
//                chart.increaseSectionShare("Weekend", (double) membersSize);
                continue;
            }

            AtomicReference<Integer> membersAbsent = new AtomicReference<>(0);
            records.forEach(absenceRecord -> {
                if (absenceRecord.inPeriod(currentDate)) {
                    chart.increaseSectionShare(absenceRecord.getAbsenceType().getRepresentation());
                    membersAbsent.updateAndGet(membersCount -> membersCount + 1);
                }
            });
            chart.increaseSectionShare("Available", (double) membersSize - membersAbsent.get());
        }
        chart.buildResponse();

        UserMessage userMessage = UserMessage.success("Chart was generated");
        userMessage.addResponseDataEntry("chart", chart);

        return userMessage;
    }

    private Boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        int dayOfWeekId = calendar.get(Calendar.DAY_OF_WEEK);

        return dayOfWeekId == Calendar.SATURDAY || dayOfWeekId == Calendar.SUNDAY;
    }

    private Long getOneDayMillis() {
        return TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
    }

    private List<AbsenceRecord> getAbsenceRecordsForPeriod(Date startDate, Date endDate, Organization organization) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR, -1);

        Date newStartDate = calendar.getTime();

        calendar.setTime(endDate);
        calendar.add(Calendar.HOUR, 1);

        Date newEndDate = calendar.getTime();

        return absenceRecordRepository.findAllByOrganizationAndStartDateAfterAndEndDateBefore(organization, newStartDate, newEndDate);
    }

    private boolean validForReview(AbsenceRecord absenceRecord) {
        return !absenceRecord.getIsCancelled() && !absenceRecord.getIsApproved();
    }

    private void validate(AbsenceRecord absenceRecord, User currentUser, Long organizationId) {
        if (!Objects.equals(absenceRecord.getMember().getUserId(), currentUser.getUserId())) {
            throw new GeneralException("Insufficient user");
        }

        if (!Objects.equals(absenceRecord.getOrganization().getOrganizationId(), organizationId)) {
            throw new GeneralException("Insufficient organization");
        }
    }

    private AbsenceRecord findById(Long absenceRecordId) {
        return absenceRecordRepository.findById(absenceRecordId)
                .orElseThrow(() -> new GeneralException("Absence record was not found"));
    }

    private Boolean isAbsenceAlreadyRequested(AbsenceRequestForm form, Organization organization, User member) {
        List<AbsenceRecord> records = absenceRecordRepository.findAllByOrganizationAndMember(organization, member);
        long newAbsenceStartTime = form.getStartDate().getTime();
        long newAbsenceEndTime = form.getStartDate().getTime();

        return records.stream()
                .filter(absenceRecord -> !absenceRecord.getIsCancelled())
                .anyMatch(absenceRecord ->
                        overlapsExistingAbsence(newAbsenceStartTime, absenceRecord)
                        || overlapsExistingAbsence(newAbsenceEndTime, absenceRecord));
    }

    private Boolean overlapsExistingAbsence(Long time, AbsenceRecord absenceRecord) {
        return time >= absenceRecord.getStartDate().getTime() && time <= absenceRecord.getEndDate().getTime();
    }

    private Boolean isRequestFormInvalid(AbsenceRequestForm form) {
        return form.getStartDate().getTime() > form.getEndDate().getTime();
    }
}
