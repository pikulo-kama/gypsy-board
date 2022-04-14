package com.adrabazha.gypsy.board.listener;

import com.adrabazha.gypsy.board.token.domain.RegistrationToken;
import com.adrabazha.gypsy.board.dto.MailMessage;
import com.adrabazha.gypsy.board.token.context.RegistrationTokenContext;
import com.adrabazha.gypsy.board.event.RegistrationCompletedEvent;
import com.adrabazha.gypsy.board.service.MailService;
import com.adrabazha.gypsy.board.token.service.RegistrationTokenService;
import com.adrabazha.gypsy.board.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collections;

@Component
public class OnRegistrationCompletedEventListener implements ApplicationListener<RegistrationCompletedEvent> {

    private final MailService mailService;
    private final RegistrationTokenService registrationTokenService;

    @Autowired
    public OnRegistrationCompletedEventListener(MailService mailService, RegistrationTokenService registrationTokenService) {
        this.mailService = mailService;
        this.registrationTokenService = registrationTokenService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompletedEvent event) {
        String baseUrl = HttpUtils.getBaseUrlFromRequest(event.getRequest());

        RegistrationTokenContext context = RegistrationTokenContext.builder()
                .user(event.getUser())
                .build();
        RegistrationToken token = registrationTokenService.createToken(context);

        MailMessage mailMessage = MailMessage.builder()
                .recipients(Collections.singletonList(event.getUser().getEmail()))
                .subject("Please confirm your email address")
                .text(String.format(
                        "To activate your account please follow this link:\n" +
                        "%s/auth/confirmEmail?token=%s",
                        baseUrl, token.getToken())
                ).build();

        mailService.sendMessage(mailMessage);
    }
}
