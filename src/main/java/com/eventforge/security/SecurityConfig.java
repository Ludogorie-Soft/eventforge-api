package com.eventforge.security;

import com.eventforge.enums.Role;
import com.eventforge.security.jwt.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    @Autowired
    private JWTAuthenticationFilter authenticationFilter;
    @Autowired
    private  MyUserDetailsService userDetailsService;

    private static final String[] SECURED_URLs = {"/admin/**", "/organisation/**"};
    private static final String[] UNSECURED_URLs = {"/registration", "/menu/**", "/login", "/authenticate"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable().cors().disable()
                .authorizeHttpRequests().requestMatchers(UNSECURED_URLs).permitAll()
                .and().authorizeHttpRequests().requestMatchers("/logout").authenticated().and()
                .authorizeHttpRequests().requestMatchers(SECURED_URLs).hasAuthority(Role.ADMIN.toString())
                .anyRequest().authenticated()
                .and().httpBasic().disable().formLogin().disable().logout().logoutUrl("/logout").addLogoutHandler(new SecurityContextLogoutHandler()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authenticationProvider(authenticationProvider()).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }
}
