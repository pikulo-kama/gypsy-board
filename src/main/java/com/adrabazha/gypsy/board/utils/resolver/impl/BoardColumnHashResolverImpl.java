package com.adrabazha.gypsy.board.utils.resolver.impl;

import com.adrabazha.gypsy.board.utils.resolver.QueryHashSet;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
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

    @Override
    public Resolver getResolverType() {
        return Resolver.BOARD_COLUMN;
    }
}
