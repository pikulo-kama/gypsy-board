package com.adrabazha.gypsy.board.event;

import com.adrabazha.gypsy.board.domain.sql.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

@Data
@Builder
public class RegistrationCompletedEvent extends ApplicationEvent {

    private User user;

    private HttpServletRequest request;

    public RegistrationCompletedEvent(User user, HttpServletRequest request) {
        super(user);
        this.user = user;
        this.request = request;
    }
}
