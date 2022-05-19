package com.adrabazha.gypsy.board.token.service;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.repository.OrganizationRepository;
import com.adrabazha.gypsy.board.service.OrganizationService;
import com.adrabazha.gypsy.board.service.UserService;
import com.adrabazha.gypsy.board.token.context.MembershipTokenContext;
import com.adrabazha.gypsy.board.token.domain.MembershipToken;
import com.adrabazha.gypsy.board.token.repository.MembershipTokenRepository;
import com.adrabazha.gypsy.board.utils.TokenUtils;
import com.adrabazha.gypsy.board.utils.mail.CustomEventPublisher;
import com.adrabazha.gypsy.board.utils.mail.templates.MessageTemplates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MembershipTokenServiceImpl implements MembershipTokenService {

    @Value("${application.token.lifetime}")
    private Integer tokenLifetimeHours;

    private final MembershipTokenRepository membershipTokenRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationService organizationService;
    private final CustomEventPublisher eventPublisher;
    private final UserService userService;

    public MembershipTokenServiceImpl(MembershipTokenRepository membershipTokenRepository,
                                      OrganizationRepository organizationRepository,
                                      OrganizationService organizationService,
                                      CustomEventPublisher eventPublisher, UserService userService) {
        this.membershipTokenRepository = membershipTokenRepository;
        this.organizationRepository = organizationRepository;
        this.organizationService = organizationService;
        this.eventPublisher = eventPublisher;
        this.userService = userService;
    }

    @Override
    public MembershipToken createToken(MembershipTokenContext context) {
        MembershipToken token = MembershipToken.builder()
                .expiryDate(TokenUtils.getExpiryDate(tokenLifetimeHours))
                .token(UUID.randomUUID().toString())
                .organizationId(context.getOrganizationId())
                .userId(context.getUserId())
                .build();

        return membershipTokenRepository.save(token);
    }

    @Override
    public void cleanExpiredTokens() {
        List<MembershipToken> expiredTokens = membershipTokenRepository.getAllByExpiryDateBefore(new Date());
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

        if (TokenUtils.isExpired(tokenUnwrapped)) {
            return false;
        }

        organizationRepository.updateUserInOrganization(tokenUnwrapped.getUserId(), tokenUnwrapped.getOrganizationId(), true);
        membershipTokenRepository.delete(tokenUnwrapped);
        User user = userService.findById(tokenUnwrapped.getUserId());

        Organization organization = organizationService.findById(tokenUnwrapped.getOrganizationId());
        eventPublisher.publishOrganizationRelatedEvent(this, organization,
                user, MessageTemplates.memberAcceptedInviteToOrganization());

        return true;
    }
}
