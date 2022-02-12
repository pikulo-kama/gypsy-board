package com.adrabazha.gypsy.board.utils.resolver;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class BoardHashResolverImpl extends BaseHashResolver<Long> implements BoardHashResolver{

    private QueryHashSet<Long> boardHashSet = QueryHashSet.empty();

    @Override
    public Long retrieveIdentifier(String hash) {
        return super.retrieveIdentifier(hash, boardHashSet);
    }

    @Override
    public String obtainHash(Long identifier) {
        return super.obtainHash(identifier, boardHashSet);
    }
}
