package com.mintyn.cardservice.security;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mintyn.cardservice.entity.User;
import com.mintyn.cardservice.repository.UserRepository;

@Service
public class TokenUtils {

    @Autowired
    private UserRepository userRepository;

    @Value("${TOKEN_SECRET}")
    private String secret;

    @Value("${TOKEN_DURATION}")
    private int duration;

    private Date setExpiryDate() {
        return new Date(System.currentTimeMillis() + duration);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secret.getBytes());
    }

    private JWTVerifier getVerifier() {
        return JWT.require(getAlgorithm()).build();
    }

    private DecodedJWT getDecodedJWT(String token) {

        if (Optional.ofNullable(token).isEmpty()) {
            throw new IllegalAccessError("An empty token was presented to be decoded");
        }

        return getVerifier().verify(token.trim());
    }

    public String getUsername(String token) {
        return getDecodedJWT(token).getSubject();
    }

    public void getClaims(String token) {}

    public Date getExpiryDate(String token) {
        return getDecodedJWT(token).getExpiresAt();
    }

    public boolean isTokenExpired(String token) {
        return getExpiryDate(token).before(new Date());
    }

    public String createToken(String subject) {
        return JWT.create().withSubject(subject).withExpiresAt(setExpiryDate()).sign(getAlgorithm());
    }

    public String createToken(String subject, Map<String, Object> claims) {
        return JWT.create().withSubject(subject).withPayload(claims).withExpiresAt(setExpiryDate()).sign(getAlgorithm());
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) throws AccountNotFoundException {
        if (Optional.ofNullable(token).isEmpty()) {
            throw new IllegalAccessError("An empty token was presented to be decoded");
        }

        // retrieve the uuid stored as username
        String username = getUsername(token);

        if (Optional.ofNullable(username).isEmpty()) {
            throw new IllegalAccessError("Token does not contain username");
        }
        
        User user = userRepository.findByUuid(username).orElseThrow(AccountNotFoundException::new);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
    }

    public Optional<User> getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return userRepository.findByUuid(authentication.getPrincipal().toString());
    }
}
