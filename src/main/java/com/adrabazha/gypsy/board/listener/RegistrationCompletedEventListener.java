package com.adrabazha.gypsy.board.listener;

import com.adrabazha.gypsy.board.domain.auth.RegistrationToken;
import com.adrabazha.gypsy.board.dto.MailMessage;
import com.adrabazha.gypsy.board.event.RegistrationCompletedEvent;
import com.adrabazha.gypsy.board.service.MailService;
import com.adrabazha.gypsy.board.service.auth.RegistrationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class RegistrationCompletedEventListener implements ApplicationListener<RegistrationCompletedEvent> {

    private final MailService mailService;
    private final RegistrationTokenService registrationTokenService;

    @Autowired
    public RegistrationCompletedEventListener(MailService mailService, RegistrationTokenService registrationTokenService) {
        this.mailService = mailService;
        this.registrationTokenService = registrationTokenService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompletedEvent event) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(event.getRequest())
                .replacePath(null)
                .build()
                .toUriString();

        RegistrationToken token = registrationTokenService.createToken(event.getUser());

        MailMessage mailMessage = new MailMessage();
        mailMessage.addRecipients(event.getUser().getEmail());
        mailMessage.setSubject("Please confirm your email address");

        mailMessage.setText(String.format(
                "To activate your account please follow this link:\n" +
                "%s/auth/confirmEmail?token=%s", baseUrl, token.getToken()));
        mailService.sendMessage(mailMessage);
    }
}
