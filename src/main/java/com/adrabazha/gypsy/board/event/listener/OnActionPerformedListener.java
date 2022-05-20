package com.adrabazha.gypsy.board.event.listener;

import com.adrabazha.gypsy.board.utils.mail.MailMessageProvider;
import com.adrabazha.gypsy.board.service.MailService;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnActionPerformedListener<E extends ApplicationEvent & MailMessageProvider>
        implements ApplicationListener<E> {

    private final MailService mailService;

    public OnActionPerformedListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(E event) {
        mailService.sendMessageAsync(event.getMailMessage());
    }
}
