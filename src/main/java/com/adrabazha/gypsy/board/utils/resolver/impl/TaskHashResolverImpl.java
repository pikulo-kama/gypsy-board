package com.adrabazha.gypsy.board.utils.resolver.impl;

import com.adrabazha.gypsy.board.utils.resolver.QueryHashSet;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class TaskHashResolverImpl extends BaseHashResolver<Long>
        implements TaskHashResolver {

    private QueryHashSet<Long> taskHashSet = QueryHashSet.empty();

    @Override
    public Long retrieveIdentifier(String hash) {
        return super.retrieveIdentifier(hash, taskHashSet);
    }

    @Override
    public String obtainHash(Long identifier) {
        return super.obtainHash(identifier, taskHashSet);
    }

    @Override
    public Resolver getResolverType() {
        return Resolver.TASK;
    }
}
