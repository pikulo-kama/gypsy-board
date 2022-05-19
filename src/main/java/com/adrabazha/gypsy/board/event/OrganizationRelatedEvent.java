package com.adrabazha.gypsy.board.event;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.MailMessage;
import com.adrabazha.gypsy.board.utils.mail.MailMessageProvider;
import com.adrabazha.gypsy.board.utils.mail.templates.MailMessageTemplate;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class OrganizationRelatedEvent extends ApplicationEvent
        implements MailMessageProvider {

    @Getter
    private final Organization organization;

    @Getter
    private final User memberPerformed;

    private final MailMessageTemplate<OrganizationRelatedEvent> messageTemplate;

    public OrganizationRelatedEvent(Object source,
                                    Organization organization,
                                    User memberPerformed,
                                    MailMessageTemplate<OrganizationRelatedEvent> messageTemplate) {
        super(source);
        this.organization = organization;
        this.memberPerformed = memberPerformed;
        this.messageTemplate = messageTemplate;
    }

    @Override
    public MailMessage getMailMessage() {
        MailMessage message = messageTemplate.apply(this);
        message.addRecipients(organization);
        message.excludeRecipient(memberPerformed.getEmail());

        return message;
    }
}
