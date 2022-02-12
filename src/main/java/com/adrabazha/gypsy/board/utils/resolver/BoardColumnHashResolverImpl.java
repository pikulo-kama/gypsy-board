package com.adrabazha.gypsy.board.utils.resolver;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class BoardColumnHashResolverImpl extends BaseHashResolver<Long>
        implements BoardColumnHashResolver {

    private QueryHashSet<Long> boardColumnHashSet = QueryHashSet.empty();

    @Override
    public Long retrieveIdentifier(String hash) {
        return super.retrieveIdentifier(hash, boardColumnHashSet);
    }

    @Override
    public String obtainHash(Long identifier) {
        return super.obtainHash(identifier, boardColumnHashSet);
    }
}
