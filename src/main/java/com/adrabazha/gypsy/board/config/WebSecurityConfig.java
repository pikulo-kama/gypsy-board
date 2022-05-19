package com.adrabazha.gypsy.board.config;

import com.adrabazha.gypsy.board.handler.AuthFailureHandler;
import com.adrabazha.gypsy.board.handler.AuthSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PAGE_URL = "/login";
    private static final List<String> ALLOWED_ROUTES = List.of(
            LOGIN_PAGE_URL,
            "/css/**",
            "/scripts/**",
            "/auth/**",
            "/webjars/**",
            "/images/**",
            "/organizations/acceptInvitation"
    );

    private final AuthFailureHandler authFailureHandler;
    private final AuthSuccessHandler authSuccessHandler;

    public WebSecurityConfig(AuthFailureHandler authFailureHandler,
                             AuthSuccessHandler authSuccessHandler) {
        this.authFailureHandler = authFailureHandler;
        this.authSuccessHandler = authSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ALLOWED_ROUTES.toArray(new String[0])).permitAll()
                .antMatchers("/**").authenticated()
                    .and()
                .formLogin()
                .loginPage(LOGIN_PAGE_URL)
                .successForwardUrl("/")
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .permitAll()
                    .and()
                .logout()
                .logoutUrl("/logout").permitAll()
                    .and().cors()
                    .and().sessionManagement().invalidSessionUrl(LOGIN_PAGE_URL);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
