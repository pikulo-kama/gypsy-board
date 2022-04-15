package com.adrabazha.gypsy.board.event;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class NewMembersAddedEvent extends ApplicationEvent {

    @Getter
    private final HttpServletRequest request;

    @Getter
    private final List<User> newMembers;

    @Getter
    private final Organization organization;

    public NewMembersAddedEvent(Object source,
                                List<User> newMembers,
                                Organization organization,
                                HttpServletRequest request) {
        super(source);
        this.newMembers = newMembers;
        this.organization = organization;
        this.request = request;
    }
}
