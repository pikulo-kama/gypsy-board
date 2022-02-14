package com.adrabazha.gypsy.board.service.auth;

import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.domain.auth.RegistrationToken;
import com.adrabazha.gypsy.board.repository.RegistrationTokenRepository;
import com.adrabazha.gypsy.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public RegistrationToken createToken(User user) {
        RegistrationToken token = RegistrationToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(getExpiryDate())
                .user(user)
                .build();

        return registrationTokenRepository.save(token);
    }

    @Override
    public Boolean validateToken(String token) {
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
    public List<RegistrationToken> getExpiredTokens() {
        return registrationTokenRepository.getRegistrationTokensByExpiryDateBefore(new Date());
    }

    @Override
    public void deleteExpiredTokens() {
        List<RegistrationToken> expiredTokens = getExpiredTokens();
        registrationTokenRepository.deleteAll(expiredTokens);
    }

    private Date getExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, tokenLifetimeHours);

        return calendar.getTime();
    }

    private Boolean isExpired(RegistrationToken token) {
        long expiryDate = token.getExpiryDate().getTime();
        long now = new Date().getTime();
        return now > expiryDate;
    }
}
