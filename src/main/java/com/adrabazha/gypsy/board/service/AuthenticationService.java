package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.dto.form.RegisterForm;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    void register(RegisterForm registerForm, HttpServletRequest request);
}
