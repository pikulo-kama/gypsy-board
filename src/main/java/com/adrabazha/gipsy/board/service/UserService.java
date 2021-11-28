package com.adrabazha.gipsy.board.service;

import com.adrabazha.gipsy.board.domain.User;
import com.adrabazha.gipsy.board.service.crud.BaseService;

import java.util.List;

public interface UserService extends BaseService<User, Long, Object, Object> {
        List<User> findUsersByUsername(String username);
}
