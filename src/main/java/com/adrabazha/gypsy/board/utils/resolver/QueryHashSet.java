package com.adrabazha.gypsy.board.utils.resolver;

import com.adrabazha.gypsy.board.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class QueryHashSet<T> {

    private List<QueryHash<T>> queryHashSet;

    private QueryHashSet() {
        this.queryHashSet = new ArrayList<>();
    }

    public static <R> QueryHashSet<R> empty() {
        return new QueryHashSet<>();
    }

    public Boolean isEmpty() {
        return queryHashSet.isEmpty();
    }

    public QueryHash<T> findByHash(String hash) {
        return queryHashSet.stream()
                .filter(hashSet -> StringUtils.equals(hashSet.getHash(), hash))
                .findFirst()
                .orElseThrow(() -> new GeneralException("Can't find entity by hash"));
    }

    public QueryHash<T> findByIdentifier(T identifier) {
        return queryHashSet.stream()
                .filter(hashSet -> Objects.equals(hashSet.getIdentifier(), identifier))
                .findFirst()
                .orElse(null);
    }

    public void addHashEntry(T identifier, String hash) {
        QueryHash<T> queryHash = new QueryHash<>(identifier, hash);
        queryHashSet.add(queryHash);
    }

    public Boolean containsIdentifier(T identifier) {
        return getObjects(QueryHash::getIdentifier).contains(identifier);
    }

    public Boolean containsHash(String hash) {
        return getObjects(QueryHash::getHash).contains(hash);
    }

    private <R> List<R> getObjects(Function<QueryHash<?>, R> function) {
        return queryHashSet.stream().map(function).collect(Collectors.toList());
    }
}
