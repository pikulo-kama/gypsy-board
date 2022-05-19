package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.dto.MailMessage;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final MailSender mailSender;
    private final ExecutorService executorService;

    @Autowired
    public MailServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void sendMessageAsync(MailMessage message) {
        executorService.submit(() -> this.sendMessage(message));
    }

    @Override
    public void sendMessage(MailMessage message) {
        if (message.getRecipients().isEmpty()) {
            log.error(format("No recipients provided. Message '%s' won't be sent.", message.getSubject()));
            return;
        }

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(message.getRecipients().toArray(new String[0]));
        simpleMailMessage.setSubject("[Gypsy Board] " + message.getSubject());
        simpleMailMessage.setText(message.getText());
        try {
            mailSender.send(simpleMailMessage);
            log.info(format("Email with topic '%s' was sent to: %s",
                    message.getSubject(), message.getRecipients()));

        } catch (MailException mailException) {
            String errorMessage = format("Failed to send message '%s' to %s", message.getSubject(),
                    String.join(", ", message.getRecipients()));
            log.error(errorMessage, mailException);
        }
    }
}
