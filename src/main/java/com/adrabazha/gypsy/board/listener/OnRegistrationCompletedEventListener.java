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

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class OnRegistrationCompletedEventListener implements ApplicationListener<RegistrationCompletedEvent> {

    private final MailService mailService;
    private final RegistrationTokenService registrationTokenService;
    private final ExecutorService executorService;

    @Autowired
    public OnRegistrationCompletedEventListener(MailService mailService,
                                                RegistrationTokenService registrationTokenService) {
        this.mailService = mailService;
        this.registrationTokenService = registrationTokenService;
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void onApplicationEvent(RegistrationCompletedEvent event) {
        RegistrationTokenContext context = RegistrationTokenContext.builder()
                .user(event.getUser())
                .build();
        RegistrationToken token = registrationTokenService.createToken(context);
        executorService.submit(() -> sendMessage(event, token));
    }

    private void sendMessage(RegistrationCompletedEvent event, RegistrationToken token) {
        String baseUrl = HttpUtils.getBaseUrlFromRequest(event.getRequest());
        MailMessage mailMessage = MailMessage.builder()
                .recipients(Collections.singletonList(event.getUser().getEmail()))
                .text(String.format(
                        "To activate your account please follow this link:\n" +
                        "%s/auth/confirmEmail?token=%s",
                        baseUrl, token.getToken())
                ).build();

        mailMessage.setSubject("Please confirm your email address");
        mailService.sendMessage(mailMessage);
    }
}
