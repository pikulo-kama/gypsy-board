package com.adrabazha.gypsy.board.utils.resolver;

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
}
