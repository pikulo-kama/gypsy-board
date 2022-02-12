package com.adrabazha.gypsy.board.controller;

import com.adrabazha.gypsy.board.dto.form.RegisterForm;
import com.adrabazha.gypsy.board.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "authentication/register";
    }

    @PostMapping("/register-user")
    public String register(@Validated RegisterForm registerForm) {
        authenticationService.register(registerForm);
        return "redirect:/login";
    }
}
