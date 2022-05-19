package com.adrabazha.gypsy.board.dto;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MailMessage {

    private String subject;

    private String text;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    private List<String> recipients = new ArrayList<>();

    public void addRecipients(Organization organization) {
        if (Objects.isNull(organization.getActiveMembers())) {
            return;
        }
        List<String> organizationMembers = organization.getActiveMembers().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        this.recipients.addAll(organizationMembers);
    }

    public void addRecipient(String email) {
        this.recipients.add(email);
    }

    public void excludeRecipient(String email) {
        recipients.removeIf(recipientEmail -> recipientEmail.equals(email));
    }
}
