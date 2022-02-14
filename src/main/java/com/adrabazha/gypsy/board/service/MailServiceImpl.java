package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.dto.MailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final MailSender mailSender;

    @Autowired
    public MailServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMessage(MailMessage message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(message.getRecipients().toArray(new String[0]));
        simpleMailMessage.setSubject(message.getSubject());
        simpleMailMessage.setText(message.getText());
        mailSender.send(simpleMailMessage);
    }
}
