package com.mintyn.cardservice.resource;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mintyn.cardservice.model.ApiResponse;
import com.mintyn.cardservice.model.LoginModel;
import com.mintyn.cardservice.model.LoginResponse;
import com.mintyn.cardservice.model.RegistrationModel;
import com.mintyn.cardservice.model.RegistrationResponse;
import com.mintyn.cardservice.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthResource {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final AuthService authService;
 
    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(@Valid @RequestBody RegistrationModel model){
        log.info("Rest request to register");
        String token = authService.register(model);
        ApiResponse<RegistrationResponse> response = new ApiResponse<>(true);
        return ResponseEntity.status(HttpStatus.CREATED).header("token", token).body(response);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginModel model) {
        log.info("Rest request to login");
        String token = authService.login(model);
        ApiResponse<LoginResponse> response = new ApiResponse<LoginResponse>(true);
        return ResponseEntity.ok().header("token", token).body(response);
    }

}
