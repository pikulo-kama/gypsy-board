package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.dto.MailMessage;

public interface MailService {

    void sendMessage(MailMessage mailMessage);

    void sendMessageAsync(MailMessage mailMessage);
}
