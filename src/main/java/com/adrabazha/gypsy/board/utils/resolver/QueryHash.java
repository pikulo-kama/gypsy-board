package com.adrabazha.gypsy.board.utils.resolver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QueryHash<T> {

    private T identifier;

    private String hash;
}
