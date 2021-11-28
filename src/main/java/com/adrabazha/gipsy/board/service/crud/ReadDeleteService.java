package com.adrabazha.gipsy.board.service.crud;

import java.util.List;

public interface ReadDeleteService<T, U> {

    List<T> findAll();

    T findById(U id);

    void deleteById(U id);
}
