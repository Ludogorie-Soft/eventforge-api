package com.eventforge.security;

import com.eventforge.constants.Role;
import com.eventforge.exception.AuthenticationEntryPoint;
import com.eventforge.security.jwt.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SECURED_URLs = {"/admin/**"};
    private static final String[] UNSECURED_URLs = {"/menu/**", "/auth/**", "/api/v1/events/**" ,"/api/v1/one-time-events/**","/unauthorized/**"};

    private static final String ORGANISATION_URLs = "/organisation/**";
    private final JWTAuthenticationFilter authenticationFilter;
    private final MyUserDetailsService userDetailsService;
    private final LogoutHandler logoutHandler;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JWTAuthenticationFilter authenticationFilter,
                          MyUserDetailsService userDetailsService,
                          LogoutHandler logoutHandler,
                          PasswordEncoder passwordEncoder,
                          AuthenticationEntryPoint authenticationEntryPoint,
                          AccessDeniedHandler accessDeniedHandler
    ) {
        this.authenticationFilter = authenticationFilter;
        this.userDetailsService = userDetailsService;
        this.logoutHandler = logoutHandler;
        this.passwordEncoder = passwordEncoder;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable().cors().disable()
                .authorizeHttpRequests().requestMatchers(UNSECURED_URLs).permitAll()
                .and().authorizeHttpRequests().requestMatchers("/proba").authenticated().and()
                .authorizeHttpRequests().requestMatchers(ORGANISATION_URLs).hasAnyAuthority(Role.ORGANISATION.toString()).and()
                .authorizeHttpRequests().requestMatchers(SECURED_URLs).hasAuthority(Role.ADMIN.toString())
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic().disable().formLogin().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authenticationProvider(authenticationProvider()).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext())).and()
                .build();

    }

}
