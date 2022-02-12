package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.dto.form.RegisterForm;

public interface AuthenticationService {
    void register(RegisterForm registerForm);
}
