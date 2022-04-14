package com.adrabazha.gypsy.board.token.repository;

import com.adrabazha.gypsy.board.token.domain.MembershipToken;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MembershipTokenRepository extends CrudRepository<MembershipToken, Long> {

    Optional<MembershipToken> getByToken(@NotNull String token);

    List<MembershipToken> getMembershipTokensByExpiryDateBefore(Date date);
}
