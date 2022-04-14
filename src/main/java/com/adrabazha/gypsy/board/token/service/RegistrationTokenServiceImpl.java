package com.adrabazha.gypsy.board.token.service;

import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.token.domain.RegistrationToken;
import com.adrabazha.gypsy.board.token.context.RegistrationTokenContext;
import com.adrabazha.gypsy.board.token.repository.RegistrationTokenRepository;
import com.adrabazha.gypsy.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegistrationTokenServiceImpl implements RegistrationTokenService {

    @Value("${application.token.lifetime}")
    private Integer tokenLifetimeHours;

    private final RegistrationTokenRepository registrationTokenRepository;
    private final UserService userService;

    @Autowired
    public RegistrationTokenServiceImpl(RegistrationTokenRepository registrationTokenRepository, UserService userService) {
        this.registrationTokenRepository = registrationTokenRepository;
        this.userService = userService;
    }

    @Override
    public Boolean validate(String token) {
        Optional<RegistrationToken> registrationToken = registrationTokenRepository.getByToken(token);
        if (registrationToken.isEmpty()) {
            return false;
        }

        RegistrationToken tokenUnwrapped = registrationToken.get();
        if (isExpired(tokenUnwrapped)) {
            return false;
        }

        User user = tokenUnwrapped.getUser();
        user.setIsEnabled(true);
        userService.save(user);

        registrationTokenRepository.delete(tokenUnwrapped);
        return true;
    }

    @Override
    public RegistrationToken createToken(RegistrationTokenContext context) {
        RegistrationToken token = RegistrationToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(getExpiryDate(tokenLifetimeHours))
                .user(context.getUser())
                .build();

        return registrationTokenRepository.save(token);
    }

    @Override
    public void cleanExpiredTokens() {
        List<RegistrationToken> expiredTokens = getExpiredTokens();
        List<User> usersToDelete = expiredTokens.stream()
                .map(RegistrationToken::getUser)
                .collect(Collectors.toList());

        userService.deleteUsers(usersToDelete);
        registrationTokenRepository.deleteAll(expiredTokens);
    }

    private List<RegistrationToken> getExpiredTokens() {
        return registrationTokenRepository.getRegistrationTokensByExpiryDateBefore(new Date());
    }
}
