package com.adrabazha.gypsy.board.controller;

import com.adrabazha.gypsy.board.dto.form.RegisterForm;
import com.adrabazha.gypsy.board.service.AuthenticationService;
import com.adrabazha.gypsy.board.service.auth.RegistrationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RegistrationTokenService registrationTokenService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, RegistrationTokenService registrationTokenService) {
        this.authenticationService = authenticationService;
        this.registrationTokenService = registrationTokenService;
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "authentication/register";
    }

    @PostMapping("/register-user")
    public String register(@Validated RegisterForm registerForm, HttpServletRequest request) {
        authenticationService.register(registerForm, request);
        return "redirect:/login";
    }

    @GetMapping("/confirmEmail")
    public String confirmEmail(@RequestParam("token") String token) {
        Boolean valid = registrationTokenService.validateToken(token);
        String returnPage = valid ? "/login" : "/error";
        return "redirect:" + returnPage;
    }
}
