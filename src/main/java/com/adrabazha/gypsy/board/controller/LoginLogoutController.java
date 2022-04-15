package com.adrabazha.gypsy.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class LoginLogoutController {

    private static final String ERROR_MESSAGE_ATTRIBUTE = "error_message";
    private static final String SUCCESS_MESSAGE_ATTRIBUTE = "success_message";

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {

        if (Objects.nonNull(error)) {
            model.addAttribute(ERROR_MESSAGE_ATTRIBUTE, error);
        }

        if (Objects.nonNull(logout)) {
            model.addAttribute(SUCCESS_MESSAGE_ATTRIBUTE, "You have successfully logged out!");
        }

        return "authentication/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "authentication/logout";
    }
}
