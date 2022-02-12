package com.adrabazha.gypsy.board.utils.resolver;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class OrganizationHashResolverImpl extends BaseHashResolver<Long>
        implements OrganizationHashResolver {

    private QueryHashSet<Long> organizationHashSet = QueryHashSet.empty();

    @Override
    public Long retrieveIdentifier(String hash) {
        return super.retrieveIdentifier(hash, organizationHashSet);
    }

    @Override
    public String obtainHash(Long identifier) {
        return super.obtainHash(identifier, organizationHashSet);
    }
}