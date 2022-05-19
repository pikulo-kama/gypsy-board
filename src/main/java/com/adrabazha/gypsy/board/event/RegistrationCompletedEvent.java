package com.adrabazha.gypsy.board.event;

import com.adrabazha.gypsy.board.domain.sql.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

public class RegistrationCompletedEvent extends ApplicationEvent {

    @Getter
    private User user;

    @Getter
    private HttpServletRequest request;

    public RegistrationCompletedEvent(Object source, User user, HttpServletRequest request) {
        super(source);
        this.user = user;
        this.request = request;
    }
}
