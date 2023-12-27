package com.mintyn.cardservice.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mintyn.cardservice.entity.User;
import com.mintyn.cardservice.model.LoginModel;
import com.mintyn.cardservice.model.RegistrationModel;
import com.mintyn.cardservice.repository.UserRepository;
import com.mintyn.cardservice.security.CustomUserDetailsService;
import com.mintyn.cardservice.security.TokenUtils;
import com.mintyn.cardservice.service.AuthService;
import com.mintyn.cardservice.service.mapping.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtils tokenUtils;
    private final AuthenticationProvider authenticationProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserMapper userMapper;
    
    @Override
    public String login(LoginModel model) {
        log.info("Request to login");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(model.getLogin(), model.getPassword());
        authenticationProvider.authenticate(authentication);
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(model.getLogin()); 
        if (Optional.ofNullable(userDetails).isEmpty()) {
            throw new IllegalStateException("An error occured");
        }
        return tokenUtils.createToken(userDetails.getUsername());
    }

    @Override
    public String register(RegistrationModel model) {
        log.info("Request to register");

        if (!model.getPassword().equals(model.getPasswordRetyped())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = userMapper.fromRegistrationModel(model);
        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(model.getPassword()));
        user = userRepository.save(user);
        return tokenUtils.createToken(user.getUuid());
    }

}
