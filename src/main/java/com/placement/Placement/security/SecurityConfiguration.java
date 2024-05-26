package com.placement.Placement.security;


import com.placement.Placement.constant.ERole;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final AuthTokenFilter authTokenFilter;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    private static final String[] WHITE_LIST_URL = {
            "/api/auth/**",
            "/api/batch/all",
            "/api/education/all"
    };

    private static final String[] CUSTOMER_LIST_URL = {
            "/api/user_placement/join",
            "/api/user_placement/{id}",
            "/api/company/all",
            "/api/customer/{id}",
            "/api/customer",
            "/api/message/customer/**",
            "/api/test/page",
            "/api/test/all",
    };

    private static final String[] SUPER_ADMIN_LIST_URL = {
            "/api/admin/**",
            "/api/user_placement/**",
            "/api/batch/**",
            "/api/company/**",
            "/api/customer/**",
            "/api/education/**",
            "/api/message/**",
            "/api/stage/**",
            "/api/test/**"
    };

    private static final String[] ADMIN_LIST_URL = {
            "/api/admin",
            "/api/user_placement/**",
            "/api/company/**",
            "/api/message/**",
            "/api/stage/**",
            "/api/test/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                .requestMatchers(CUSTOMER_LIST_URL).hasAuthority(String.valueOf(ERole.ROLE_CUSTOMER))
                                .requestMatchers(ADMIN_LIST_URL).hasAuthority(String.valueOf(ERole.ROLE_ADMIN))
                                .requestMatchers(SUPER_ADMIN_LIST_URL).hasAuthority(String.valueOf(ERole.ROLE_SUPER_ADMIN))
                                .requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
