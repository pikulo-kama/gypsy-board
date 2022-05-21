package com.adrabazha.gypsy.board.utils.resolver;

public interface HashResolver<T> {

    T retrieveIdentifier(String hash);

    String obtainHash(T identifier);

    Resolver getResolverType();
}
