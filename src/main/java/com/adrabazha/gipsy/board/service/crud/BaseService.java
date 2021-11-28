package com.adrabazha.gipsy.board.service.crud;

public interface BaseService<T, U, CF, UF> extends ReadDeleteService<T, U>,
                                                   CreateOnlyService<T, CF>,
                                                   UpdateOnlyService<T, UF, U> {
}
