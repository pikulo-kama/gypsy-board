package com.adrabazha.gypsy.board.listener;

import com.adrabazha.gypsy.board.domain.Organization;
import com.adrabazha.gypsy.board.domain.User;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class OnNewMembersAddedEventListener implements ApplicationListener<NewMembersAddedEvent> {

    private final MailService mailService;
    private final MembershipTokenService membershipTokenService;
    private final ExecutorService executorService;

    public OnNewMembersAddedEventListener(MailService mailService, MembershipTokenService membershipTokenService) {
        this.mailService = mailService;
        this.membershipTokenService = membershipTokenService;
        this.executorService = Executors.newCachedThreadPool();
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
            executorService.submit(() ->
                    sendInvitation(member, event.getOrganization(), token, baseUrl));
        });
    }

    private void sendInvitation(User member, Organization organization, MembershipToken token, String baseUrl) {
        MailMessage message = MailMessage.builder()
                .recipients(Collections.singletonList(member.getEmail()))
                .subject(String.format("Invitation to '%s'", organization.getOrganizationName()))
                .text(String.format(
                        "You was invited to '%s'\n" +
                                "Follow this link to accept invitation\n" +
                                "%s/organizations/acceptInvitation?token=%s",
                        organization.getOrganizationName(), baseUrl, token.getToken()
                )).build();

        mailService.sendMessage(message);
    }
}
