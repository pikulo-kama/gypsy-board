package com.adrabazha.gypsy.board.utils.mail;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.event.MemberRelatedEvent;
import com.adrabazha.gypsy.board.event.NewMembersAddedEvent;
import com.adrabazha.gypsy.board.event.OrganizationRelatedEvent;
import com.adrabazha.gypsy.board.event.RegistrationCompletedEvent;
import com.adrabazha.gypsy.board.utils.mail.templates.MailMessageTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class CustomEventPublisherImpl implements CustomEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public CustomEventPublisherImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publishOrganizationRelatedEvent(Object source,
                                                Organization organization,
                                                User memberPerformed,
                                                MailMessageTemplate<OrganizationRelatedEvent> messageTemplate) {
        OrganizationRelatedEvent event = new OrganizationRelatedEvent(source, organization, memberPerformed, messageTemplate);
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publishMemberRelatedEvent(Object source,
                                          Organization organization,
                                          User receiver,
                                          User memberPerformed,
                                          MailMessageTemplate<MemberRelatedEvent> messageTemplate) {
        MemberRelatedEvent event = new MemberRelatedEvent(source, organization, receiver, memberPerformed, messageTemplate);
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publishRegistrationCompletedEvent(Object source, User user, HttpServletRequest request) {
        RegistrationCompletedEvent event = new RegistrationCompletedEvent(source, user, request);
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publishNewMembersAddedEvent(Object source,
                                            List<User> newMembers,
                                            Organization organization,
                                            HttpServletRequest request) {
        NewMembersAddedEvent event = new NewMembersAddedEvent(source, newMembers, organization, request);
        eventPublisher.publishEvent(event);
    }
}
