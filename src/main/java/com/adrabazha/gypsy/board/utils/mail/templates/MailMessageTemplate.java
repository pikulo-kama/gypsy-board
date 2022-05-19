package com.adrabazha.gypsy.board.utils.mail.templates;

import com.adrabazha.gypsy.board.dto.MailMessage;
import com.adrabazha.gypsy.board.utils.mail.MailMessageProvider;

import java.util.function.Function;

public interface MailMessageTemplate<T extends MailMessageProvider> extends Function<T, MailMessage> {
}
