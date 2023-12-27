package com.mintyn.cardservice.service;

import com.mintyn.cardservice.model.LoginModel;
import com.mintyn.cardservice.model.RegistrationModel;

public interface AuthService {

    String login(LoginModel model);
    String register(RegistrationModel model);
}
