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
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private MySavedRequestAwareAuthenticationSuccessHandler mySavedRequestAwareAuthenticationSuccessHandler;

    @Autowired
    private SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler()
    {
        return new SimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint()
    {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public MySavedRequestAwareAuthenticationSuccessHandler mySavedRequestAwareAuthenticationSuccessHandler()
    {
        return new MySavedRequestAwareAuthenticationSuccessHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler()
    {
        return new LogoutSuccessHandler();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception
    {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from USERS where username = ?")
                .authoritiesByUsernameQuery("select u.username, au.authority from USERS u, Authorities au where u.id = au.user_id and u.username = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/index.*", "/**/*.css", "/**/*.js", "/**/*.ico", "/**/*.png", "/**/*.ttf", "/**/*.woff", "/**/*.woff2").permitAll()
                .antMatchers("/authn/signup", "/authn/forgot-password", "/authn/verify-token").permitAll()
                .antMatchers("/authn/reset-password").hasAuthority(AuthorizationPrivilege.CHANGE_PASSWORD)
                .antMatchers("/*").hasAnyAuthority("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(mySavedRequestAwareAuthenticationSuccessHandler)
                .failureHandler(simpleUrlAuthenticationFailureHandler)
                .and()
                .logout().logoutSuccessHandler(logoutSuccessHandler);
    }
}
