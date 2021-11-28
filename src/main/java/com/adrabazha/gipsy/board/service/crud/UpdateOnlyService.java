package com.adrabazha.gipsy.board.service.crud;

public interface UpdateOnlyService<T, F, U> {
    T updateById(U id, F form);
}
