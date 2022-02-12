package com.adrabazha.gypsy.board.utils.resolver;

import com.adrabazha.gypsy.board.exception.GeneralException;

import java.util.UUID;

public class BaseHashResolver<T> {

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
            hash = UUID.randomUUID().toString();
            hashSet.addHashEntry(identifier, hash);
        }
        return hash;
    }
}
