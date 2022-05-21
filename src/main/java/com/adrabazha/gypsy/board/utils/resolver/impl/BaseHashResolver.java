package com.adrabazha.gypsy.board.utils.resolver.impl;

import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.utils.resolver.QueryHashSet;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;

import java.util.UUID;

abstract class BaseHashResolver<T> {

    protected T retrieveIdentifier(String hash, QueryHashSet<T> hashSet) {
        if (hashSet.isEmpty() || !hashSet.containsHash(hash)) {
            throw new GeneralException(String.format(
                    "Error occurred can't find object id by hash [%s %s]",
                    getClass().getCanonicalName(), hash));
        }
        return hashSet.findByHash(hash).getIdentifier();
    }

    protected String obtainHash(T identifier, QueryHashSet<T> hashSet) {
        String hash;

        if (hashSet.containsIdentifier(identifier)) {
            hash = hashSet.findByIdentifier(identifier).getHash();
        } else {
            hash = String.format("%s-%s",
                    getResolverType().getPrefix(), UUID.randomUUID().toString());
            hashSet.addHashEntry(identifier, hash);
        }
        return hash;
    }

    public abstract Resolver getResolverType();
}
