package com.mintyn.cardservice.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mintyn.cardservice.entity.User;
import com.mintyn.cardservice.exception.AccountNotFoundException;
import com.mintyn.cardservice.model.LoginModel;
import com.mintyn.cardservice.model.RegistrationModel;
import com.mintyn.cardservice.repository.UserRepository;
import com.mintyn.cardservice.security.CustomUserDetails;
import com.mintyn.cardservice.security.CustomUserDetailsService;
import com.mintyn.cardservice.security.TokenUtils;
import com.mintyn.cardservice.service.impl.AuthServiceImpl;
import com.mintyn.cardservice.service.mapping.UserMapper;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthServiceImplTest {

    private final String TOKEN = "toks";
    private final String EMAIL = "test@test.com";
    private final String PASSWORD = "pasta";
    private final String UID = UUID.randomUUID().toString();

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsernamePasswordAuthenticationToken authenticationToken;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private User user;

    @BeforeEach
    public void init(TestInfo testInfo) {
        user = user();
        log.info("{}Starting {}{}", "-".repeat(10), testInfo.getDisplayName(), "-".repeat(10));
    }

    public User user() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setUuid(UID);

        return user;
    }

    @Test
    public void loginTest() throws Exception{
        LoginModel model = new LoginModel();
        model.setLogin(EMAIL);
        model.setPassword(PASSWORD);

        
        UserDetails userDetails = new CustomUserDetails(user);
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        when(tokenUtils.createToken(any())).thenReturn(TOKEN);
        
        String result = authService.login(model);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(TOKEN);

        log.info("result: {}", result);
    }

    @Test
    public void loginWithBadCredentialsTest() throws Exception{
        LoginModel model = new LoginModel();
        model.setLogin("login@test.com");
        model.setPassword("pasta");

        when(customUserDetailsService.loadUserByUsername(any())).thenThrow(AccountNotFoundException.class);
        when(tokenUtils.createToken(any())).thenReturn(TOKEN);
        
        assertThrows(AccountNotFoundException.class, () -> {
            authService.login(model);
        });
    }

    @Test
    public void registerTest() throws Exception{

        RegistrationModel model = new RegistrationModel();
        model.setEmail(EMAIL);
        model.setPassword(PASSWORD);
        model.setPasswordRetyped(PASSWORD);

        when(userMapper.fromRegistrationModel(any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(tokenUtils.createToken(any())).thenReturn(TOKEN);

        String result = authService.register(model);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(TOKEN);

        verify(userRepository, times(1)).save(any());
        
        log.info("result: {}", result);
    }

    @Test
    public void registerWithPasswordMisMathedTest() throws Exception{
        RegistrationModel model = new RegistrationModel();
        model.setEmail(EMAIL);
        model.setPassword(PASSWORD);
        model.setPasswordRetyped(PASSWORD.concat(EMAIL));

        assertThrows(IllegalArgumentException.class, ()->{
            authService.register(model);
        });

        verify(userRepository, never()).save(any());
    }

}
