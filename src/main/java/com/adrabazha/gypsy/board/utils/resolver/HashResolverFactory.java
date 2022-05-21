package com.adrabazha.gypsy.board.utils.resolver;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HashResolverFactory {

    private final Map<Resolver, HashResolver<Long>> hashResolverMap;

    public HashResolverFactory(List<HashResolver<Long>> hashResolverList) {
        this.hashResolverMap = new HashMap<>();
        hashResolverList.forEach(resolver -> hashResolverMap.putIfAbsent(resolver.getResolverType(), resolver));
    }

    public Long retrieveIdentifier(String hash) {
        return hashResolverMap.get(Resolver.fromHash(hash)).retrieveIdentifier(hash);
    }

    public String obtainHash(Long identifier, Resolver resolver) {
        return hashResolverMap.get(resolver).obtainHash(identifier);
    }
}
