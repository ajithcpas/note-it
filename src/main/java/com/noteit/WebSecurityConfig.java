package com.noteit;

import com.noteit.auth.LogoutSuccessHandler;
import com.noteit.auth.MySavedRequestAwareAuthenticationSuccessHandler;
import com.noteit.auth.RestAuthenticationEntryPoint;
import com.noteit.auth.authorization.AuthorizationPrivilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from USERS where username = ?")
                .authoritiesByUsernameQuery("select u.username, au.authority from USERS u, Authorities au where u.id = au.user_id and u.username = ?");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.*", "/*.ico", "/assets/**", "/fonts/**", "/authn/signup", "/authn/forgot-password", "/authn/verify-token").permitAll()
                        .requestMatchers("/authn/reset-password").hasAuthority(AuthorizationPrivilege.CHANGE_PASSWORD)
                        .requestMatchers("/*").hasAnyAuthority("USER")
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new RestAuthenticationEntryPoint()))
                .formLogin(form -> form
                        .successHandler(new MySavedRequestAwareAuthenticationSuccessHandler())
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler()))
                .logout(logout -> logout.logoutSuccessHandler(new LogoutSuccessHandler()));
        return httpSecurity.build();
    }
}
