package com.adrabazha.gypsy.board.token.service;

import com.adrabazha.gypsy.board.token.context.MembershipTokenContext;
import com.adrabazha.gypsy.board.token.domain.MembershipToken;
import com.adrabazha.gypsy.board.token.repository.MembershipTokenRepository;
import com.adrabazha.gypsy.board.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MembershipTokenServiceImpl implements MembershipTokenService {

    private final MembershipTokenRepository membershipTokenRepository;
    private final OrganizationRepository organizationRepository;

    @Value("${application.token.lifetime}")
    private Integer tokenLifetimeHours;

    public MembershipTokenServiceImpl(MembershipTokenRepository membershipTokenRepository, OrganizationRepository organizationRepository) {
        this.membershipTokenRepository = membershipTokenRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public MembershipToken createToken(MembershipTokenContext context) {
        MembershipToken token = MembershipToken.builder()
                .expiryDate(getExpiryDate(tokenLifetimeHours))
                .token(UUID.randomUUID().toString())
                .organizationId(context.getOrganizationId())
                .userId(context.getUserId())
                .build();

        return membershipTokenRepository.save(token);
    }

    @Override
    public void cleanExpiredTokens() {
        List<MembershipToken> expiredTokens = getExpiredTokens();
        expiredTokens.forEach(token ->
                organizationRepository.deleteConcreteUserFromOrganization(token.getOrganizationId(), token.getUserId())
        );
        membershipTokenRepository.deleteAll(expiredTokens);
    }

    @Override
    @Transactional
    public Boolean validate(String token) {
        Optional<MembershipToken> membershipToken = membershipTokenRepository.getByToken(token);

        if (membershipToken.isEmpty()) {
            return false;
        }

        MembershipToken tokenUnwrapped = membershipToken.get();

        if (isExpired(tokenUnwrapped)) {
            return false;
        }

        organizationRepository.updateUserInOrganization(tokenUnwrapped.getUserId(), tokenUnwrapped.getOrganizationId(), true);
        membershipTokenRepository.delete(tokenUnwrapped);
        return true;
    }

    private List<MembershipToken> getExpiredTokens() {
        return membershipTokenRepository.getMembershipTokensByExpiryDateBefore(new Date());
    }
}
