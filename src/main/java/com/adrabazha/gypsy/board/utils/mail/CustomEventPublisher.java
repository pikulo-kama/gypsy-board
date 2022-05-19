package com.adrabazha.gypsy.board.utils.mail;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.event.MemberRelatedEvent;
import com.adrabazha.gypsy.board.event.OrganizationRelatedEvent;
import com.adrabazha.gypsy.board.utils.mail.templates.MailMessageTemplate;
import org.springframework.context.ApplicationEventPublisher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CustomEventPublisher {

    void publishOrganizationRelatedEvent(Object source,
                                         Organization organization,
                                         User memberPerformed,
                                         MailMessageTemplate<OrganizationRelatedEvent> messageTemplate);

    void publishMemberRelatedEvent(Object source,
                                   Organization organization,
                                   User receiver,
                                   User memberPerformed,
                                   MailMessageTemplate<MemberRelatedEvent> messageTemplate);

    void publishRegistrationCompletedEvent(Object source, User user, HttpServletRequest request);

    void publishNewMembersAddedEvent(Object source,
                                     List<User> newMembers,
                                     Organization organization,
                                     HttpServletRequest request);
}
