package com.adrabazha.gypsy.board.utils.mail.templates;

import com.adrabazha.gypsy.board.domain.nosql.OrganizationDocument;
import com.adrabazha.gypsy.board.domain.sql.AbsenceRecord;
import com.adrabazha.gypsy.board.domain.sql.Board;
import com.adrabazha.gypsy.board.domain.sql.BoardColumn;
import com.adrabazha.gypsy.board.domain.sql.OrganizationRole;
import com.adrabazha.gypsy.board.domain.sql.Task;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.MailMessage;
import com.adrabazha.gypsy.board.event.MemberRelatedEvent;
import com.adrabazha.gypsy.board.event.OrganizationRelatedEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.lang.String.format;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageTemplates {

    public static MailMessageTemplate<OrganizationRelatedEvent> memberWasInvitedToOrganization(User member) {
        return event ->
                MailMessage.builder()
                        .subject(String.format("Someone was invited to '%s'",
                                event.getOrganization().getOrganizationName()))
                        .text(String.format("%s was invited to '%s' by %s", member.getFullName(),
                                event.getOrganization().getOrganizationName(),
                                event.getMemberPerformed().getFullName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> memberAcceptedInviteToOrganization() {
        return event ->
                MailMessage.builder()
                        .subject(String.format("Someone accepted invitation to '%s'",
                                event.getOrganization().getOrganizationName()))
                        .text(String.format("%s accepted invitation to '%s'",
                                event.getMemberPerformed().getFullName(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<MemberRelatedEvent> memberWasRemovedFromOrganization() {
        return event ->
                MailMessage.builder()
                        .subject(format("You was removed from '%s'",
                                event.getOrganization().getOrganizationName()))
                        .text(format("You was removed from '%s' by %s",
                                event.getOrganization().getOrganizationName(),
                                event.getMemberPerformed().getFullName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> memberWasRemovedFromOrganization(User memberRemoved) {
        return event ->
                MailMessage.builder()
                        .subject(format("Someone was removed from '%s'",
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s was removed from '%s' by %s",
                                memberRemoved.getFullName(),
                                event.getOrganization().getOrganizationName(),
                                event.getMemberPerformed().getFullName()))
                        .build();
    }

    public static MailMessageTemplate<MemberRelatedEvent> memberRoleWasUpdated(OrganizationRole newRole) {
        return event ->
                MailMessage.builder()
                        .subject(format("Your role in '%s' was updated",
                                event.getOrganization().getOrganizationName()))
                        .text(format("Your role in '%s' was changed to %s by %s",
                                event.getOrganization().getOrganizationName(),
                                newRole.getRoleName(),
                                event.getMemberPerformed().getFullName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> documentWasModified(OrganizationDocument document) {
        return event ->
                MailMessage.builder()
                        .subject(format("Document in '%s' updated by someone",
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s updated '%s' in '%s'",
                                event.getMemberPerformed().getFullName(),
                                document.getDocumentHeader(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> documentWasDeleted(OrganizationDocument document) {
        return event ->
                MailMessage.builder()
                        .subject(format("Document in '%s' deleted by someone",
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s deleted '%s' in '%s'",
                                event.getMemberPerformed().getFullName(),
                                document.getDocumentHeader(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> documentWasCreated(OrganizationDocument document) {
        return event ->
                MailMessage.builder()
                        .subject(format("Someone created new document in '%s'",
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s created '%s' in '%s'",
                                event.getMemberPerformed().getFullName(),
                                document.getDocumentHeader(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> memberRequestedAbsence(AbsenceRecord absenceRecord) {
        return event ->
                MailMessage.builder()
                        .subject(format("Someone requested absence in '%s'",
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s requested %s in '%s'",
                                event.getMemberPerformed().getFullName(),
                                absenceRecord.getAbsenceType().getRepresentation().toLowerCase(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> memberCancelledAbsence(AbsenceRecord absenceRecord) {
        return event ->
                MailMessage.builder()
                        .subject(format("Someone cancelled absence request in '%s'",
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s cancelled his/her %s request in '%s'",
                                event.getMemberPerformed().getFullName(),
                                absenceRecord.getAbsenceType().getRepresentation().toLowerCase(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> memberAbsenceRequestWasApproved(AbsenceRecord absenceRecord) {
        return event ->
                MailMessage.builder()
                        .subject(format("%s approved absence request in '%s'",
                                event.getMemberPerformed().getFullName(),
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s approved %s request submitted by %s in '%s'",
                                event.getMemberPerformed().getFullName(),
                                absenceRecord.getAbsenceType().getRepresentation().toLowerCase(),
                                absenceRecord.getMember().getFullName(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> columnCreated(BoardColumn newColumn) {
        return event ->
                MailMessage.builder()
                        .subject(format("New column was added to board '%s' in '%s'",
                                newColumn.getBoard().getBoardName(),
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s created column new column - '%s'",
                                event.getMemberPerformed().getFullName(),
                                newColumn.getColumnName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> taskCreated(Task newTask) {
        return event ->
                MailMessage.builder()
                        .subject(format("New task was added to board '%s' in '%s'",
                                newTask.getBoardColumn().getBoard().getBoardName(),
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s added task '%s' to board '%s' and placed it in column '%s'",
                                event.getMemberPerformed().getFullName(),
                                newTask.getTaskName(),
                                newTask.getBoardColumn().getBoard().getBoardName(),
                                newTask.getBoardColumn().getColumnName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> boardCreated(Board newBoard) {
        return event ->
                MailMessage.builder()
                        .subject(format("New board was created in '%s'",
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s created new board - '%s' - in '%s'",
                                event.getMemberPerformed().getFullName(),
                                newBoard.getBoardName(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> columnUpdated(BoardColumn boardColumn) {
        return event ->
                MailMessage.builder()
                        .subject(format("Board column was updated in '%s' | %s",
                                boardColumn.getBoard().getBoardName(),
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s updated board column in '%s'",
                                event.getMemberPerformed().getFullName(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> taskUpdated(Task task) {
        return event ->
                MailMessage.builder()
                        .subject(format("Task was updated in '%s' | %s",
                                task.getBoardColumn().getBoard().getBoardName(),
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s updated task '%s'  in '%s'",
                                event.getMemberPerformed().getFullName(),
                                task.getTaskName(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> boardUpdated(Board board) {
        return event ->
                MailMessage.builder()
                        .subject(format("Board was updated | %s",
                                event.getOrganization().getOrganizationName()))
                        .text(format("%s updated board '%s' in '%s'",
                                event.getMemberPerformed().getFullName(),
                                board.getBoardName(),
                                event.getOrganization().getOrganizationName()))
                        .build();
    }

    public static MailMessageTemplate<OrganizationRelatedEvent> organizationDeleted() {
        return event ->
                MailMessage.builder()
                        .subject(format("DON'T read this message, please | %s",
                                event.getOrganization().getOrganizationName()))
                        .text(format("Guys, it's very big disappointment, \n" +
                                        "but our  beautiful '%s' was deleted by %s.\n" +
                                        "It was a honor..",
                                event.getOrganization().getOrganizationName(),
                                event.getMemberPerformed().getFullName()))
                        .build();
    }
}
