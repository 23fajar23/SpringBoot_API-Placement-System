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
            "/api/education/all",
    };

    private static final String[] CUSTOMER_LIST_URL = {
            "/api/placement/all",
            "/api/placement/page",
            "/api/message/customer",
            "/api/user_placement/join",
            "/api/company/all",

    };

    private static final String[] SUPER_ADMIN_LIST_URL = {
            "/api/batch",
            "/api/education",
            "/api/super_admin",
            "/api/admin/all",
            "/api/admin/page",
    };

    private static final String[] ADMIN_LIST_URL = {
            "/api/message",
            "/api/user_placement/approve",
            "/api/user_placement",
            "/api/admin",
            "/api/company/page",
            "/api/stage",
            "/api/placement",
            "/api/company",
            "/api/company/page",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                .requestMatchers(CUSTOMER_LIST_URL).hasAnyAuthority(String.valueOf(ERole.ROLE_CUSTOMER),String.valueOf(ERole.ROLE_ADMIN),String.valueOf(ERole.ROLE_SUPER_ADMIN))
                                .requestMatchers(ADMIN_LIST_URL).hasAnyAuthority(String.valueOf(ERole.ROLE_ADMIN),String.valueOf(ERole.ROLE_SUPER_ADMIN))
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
