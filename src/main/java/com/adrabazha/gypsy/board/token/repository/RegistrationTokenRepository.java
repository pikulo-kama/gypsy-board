package com.adrabazha.gypsy.board.token.repository;

import com.adrabazha.gypsy.board.token.domain.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, Long> {

    Optional<RegistrationToken> getByToken(@NotNull String token);

    List<RegistrationToken> getRegistrationTokensByExpiryDateBefore(@NotNull Date expiryDate);
}
