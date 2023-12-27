package com.mintyn.cardservice.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginModel {
    @NotNull
    private String login;

    @NotNull
    private String password;
}
