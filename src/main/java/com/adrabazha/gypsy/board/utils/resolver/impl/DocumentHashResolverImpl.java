package com.adrabazha.gypsy.board.utils.resolver.impl;

import com.adrabazha.gypsy.board.utils.resolver.QueryHashSet;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class DocumentHashResolverImpl extends BaseHashResolver<Long> implements DocumentHashResolver {

    private QueryHashSet<Long> documentHashSet = QueryHashSet.empty();

    @Override
    public Long retrieveIdentifier(String hash) {
        return super.retrieveIdentifier(hash, documentHashSet);
    }

    @Override
    public String obtainHash(Long identifier) {
        return super.obtainHash(identifier, documentHashSet);
    }

    @Override
    public Resolver getResolverType() {
        return Resolver.DOCUMENT;
    }
}
