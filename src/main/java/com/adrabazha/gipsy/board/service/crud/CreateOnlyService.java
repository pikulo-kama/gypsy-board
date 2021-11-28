package com.adrabazha.gipsy.board.service.crud;

public interface CreateOnlyService<T, F> {

    T create(F form);
}
