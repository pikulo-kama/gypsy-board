package com.adrabazha.gypsy.board.job;

import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.domain.auth.RegistrationToken;
import com.adrabazha.gypsy.board.service.UserService;
import com.adrabazha.gypsy.board.service.auth.RegistrationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RegistrationTokenCleanerJob {

    private final RegistrationTokenService registrationTokenService;
    private final UserService userService;

    @Autowired
    public RegistrationTokenCleanerJob(RegistrationTokenService registrationTokenService, UserService userService) {
        this.registrationTokenService = registrationTokenService;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void dropExpiredTokens() {
        List<User> usersToDelete = registrationTokenService.getExpiredTokens().stream()
                .map(RegistrationToken::getUser)
                .collect(Collectors.toList());

        userService.deleteUsers(usersToDelete);
        registrationTokenService.deleteExpiredTokens();
    }
}
