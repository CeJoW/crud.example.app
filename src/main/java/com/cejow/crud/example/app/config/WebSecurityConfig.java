package com.cejow.crud.example.app.config;

import com.cejow.crud.example.app.filters.CustomAuthenticationFilter;
import com.cejow.crud.example.app.handlers.AuthenticationFailedHandler;
import com.cejow.crud.example.app.handlers.AuthenticationSuccessHandler;
import com.cejow.crud.example.app.handlers.CustomLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("UserDetailServiceId")
    UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailedHandler authenticationFailedHandler;
    @Autowired
    private CustomLogoutHandler customLogoutHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/main", "/registration").permitAll()
                    .antMatchers("/users", "/roles").authenticated()
                    .antMatchers("/users/**", "/roles/**").hasRole("ADMIN")
                   // .anyRequest().authenticated()
                .and()
                    .addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .formLogin()
                    .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    //.addLogoutHandler(customLogoutHandler)
                    .logoutSuccessHandler(customLogoutHandler)
                    .deleteCookies()
                    .clearAuthentication(true)
                    .permitAll()
//                .and()
//                    .exceptionHandling()
//                    .accessDeniedPage("/main-page")
                .and()
                    .csrf()
                    .disable();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    private CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailedHandler);
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/login");
        return filter;
    }
}
