package com.adrabazha.gipsy.board.service;

import com.adrabazha.gipsy.board.domain.User;
import com.adrabazha.gipsy.board.exception.GeneralException;
import com.adrabazha.gipsy.board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new GeneralException(String.format("User with id=%s doesn't exist", id)));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User create(Object form) {
        // todo implement after dto will be added
        return null;
    }

    @Override
    public User updateById(Long id, Object form) {
        // todo implement after dto will be added
        return null;
    }

    @Override
    public List<User> findUsersByUsername(String username) {
        return userRepository.findUsersByUsernameContains(username);
    }
}
