package com.adrabazha.gypsy.board.dto;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailMessage {

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String text;

    @Getter
    private List<String> recipients = new ArrayList<>();

    public void addRecipients(String... recipients) {
        this.recipients.addAll(Arrays.stream(recipients).collect(Collectors.toList()));
    }

    public void setSubject(String subject) {
        this.subject = String.format("[ Gypsy Board ] %s", subject);
    }

    public static MailMessage fromOrganization(Organization organization) {
        List<String> membersEmails = organization.getUsers().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        return MailMessage.builder()
                .recipients(membersEmails)
                .build();
    }
}
