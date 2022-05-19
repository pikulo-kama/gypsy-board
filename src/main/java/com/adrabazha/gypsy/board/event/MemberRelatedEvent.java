package com.adrabazha.gypsy.board.event;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.MailMessage;
import com.adrabazha.gypsy.board.utils.mail.MailMessageProvider;
import com.adrabazha.gypsy.board.utils.mail.templates.MailMessageTemplate;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class MemberRelatedEvent extends ApplicationEvent
        implements MailMessageProvider {

    @Getter
    private final Organization organization;

    @Getter
    private final User receiver;

    @Getter
    private final User memberPerformed;

    private final MailMessageTemplate<MemberRelatedEvent> messageTemplate;

    public MemberRelatedEvent(Object source,
                              Organization organization,
                              User receiver,
                              User memberPerformed,
                              MailMessageTemplate<MemberRelatedEvent> messageTemplate) {
        super(source);
        this.organization = organization;
        this.receiver = receiver;
        this.memberPerformed = memberPerformed;
        this.messageTemplate = messageTemplate;
    }

    @Override
    public MailMessage getMailMessage() {
        MailMessage message = messageTemplate.apply(this);
        message.addRecipient(receiver.getEmail());

        return message;
    }
}
