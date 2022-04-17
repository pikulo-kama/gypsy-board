package com.adrabazha.gypsy.board.utils.resolver;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class AbsenceRecordHashResolverImpl extends BaseHashResolver<Long>
        implements AbsenceRecordHashResolver {

    private QueryHashSet<Long> absenceRecordHashSet = QueryHashSet.empty();

    @Override
    public Long retrieveIdentifier(String hash) {
        return super.retrieveIdentifier(hash, absenceRecordHashSet);
    }

    @Override
    public String obtainHash(Long identifier) {
        return super.obtainHash(identifier, absenceRecordHashSet);
    }
}
