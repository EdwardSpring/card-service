package com.mintyn.cardservice.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RegistrationModel {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String passwordRetyped;

}
