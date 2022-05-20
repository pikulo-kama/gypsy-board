package com.adrabazha.gypsy.board.event.listener;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.MailMessage;
import com.adrabazha.gypsy.board.event.NewMembersAddedEvent;
import com.adrabazha.gypsy.board.service.MailService;
import com.adrabazha.gypsy.board.token.context.MembershipTokenContext;
import com.adrabazha.gypsy.board.token.domain.MembershipToken;
import com.adrabazha.gypsy.board.token.service.MembershipTokenService;
import com.adrabazha.gypsy.board.utils.HttpUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class OnNewMembersAddedListener implements ApplicationListener<NewMembersAddedEvent> {

    private final MailService mailService;
    private final MembershipTokenService membershipTokenService;

    public OnNewMembersAddedListener(MailService mailService,
                                     MembershipTokenService membershipTokenService) {
        this.mailService = mailService;
        this.membershipTokenService = membershipTokenService;
    }

    @Override
    public void onApplicationEvent(NewMembersAddedEvent event) {

        MembershipTokenContext context = MembershipTokenContext.builder()
                .organizationId(event.getOrganization().getOrganizationId())
                .build();

        String baseUrl = HttpUtils.getBaseUrlFromRequest(event.getRequest());

        event.getNewMembers().forEach(member -> {
            context.setUserId(member.getUserId());
            MembershipToken token = membershipTokenService.createToken(context);
            sendInvitation(member, event.getOrganization(), token, baseUrl);
        });
    }

    private void sendInvitation(User member, Organization organization, MembershipToken token, String baseUrl) {
        MailMessage message = MailMessage.builder()
                .recipients(Collections.singletonList(member.getEmail()))
                .text(String.format(
                        "You was invited to '%s'\n" +
                                "Follow this link to accept invitation\n" +
                                "%s/organizations/acceptInvitation?token=%s",
                        organization.getOrganizationName(), baseUrl, token.getToken()
                )).build();

        message.setSubject(String.format("Invitation to '%s'", organization.getOrganizationName()));
        mailService.sendMessageAsync(message);
    }
}
