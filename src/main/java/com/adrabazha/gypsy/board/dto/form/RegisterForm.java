package com.adrabazha.gypsy.board.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterForm {

    @NotBlank(message = "Username shouldn't be empty")
    @Pattern(regexp = "^\\w{4,20}$", message = "Only lower case letters allowed")
    private String username;

    @NotBlank(message = "Email shouldn't be empty")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "First Name shouldn't be empty")
    @Pattern(regexp = "^[A-Z][a-z]{2,10}$", message = "First Name should be capitalized lower case word(i.e. John)")
    private String firstName;

    @NotBlank(message = "Last Name shouldn't be empty")
    @Pattern(regexp = "^[A-Z][a-z]{2,10}$", message = "Last Name should be capitalized lower case word(i.e. Johnson)")
    private String lastName;

    @NotBlank(message = "Password shouldn't be empty")
    private String password;

    @NotBlank(message = "Password repeat shouldn't be empty")
    private String passwordRepeat;
}
