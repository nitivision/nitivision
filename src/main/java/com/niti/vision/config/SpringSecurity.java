package com.niti.vision.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.niti.vision.security.CustomAuthenticationFailureHandler;
import com.niti.vision.security.CustomLoginSuccessHandler;
import com.niti.vision.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SpringSecurity{

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public SpringSecurity(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,CustomAuthenticationFailureHandler failureHandler,CustomLoginSuccessHandler successHandler) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/verifyCaptcha").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/js/**").permitAll()
                                .requestMatchers("/index/**").permitAll()
                                .requestMatchers("/about").permitAll()
                                .requestMatchers("/blog").permitAll()
                               .requestMatchers("/contact").permitAll()
                                .requestMatchers("/portfolio").permitAll()
                                .requestMatchers("/services").permitAll()
                                .requestMatchers("/consulting").permitAll()
                                .requestMatchers("/manpower").permitAll()
                                .requestMatchers("/hrsolution").permitAll()
                                .requestMatchers("/career").permitAll()
                                .requestMatchers("/forgot-password").permitAll()
                                .requestMatchers("/send-otp").permitAll()
                                .requestMatchers("/web-development").permitAll()
                                .requestMatchers("/reset-password").permitAll()
                                .requestMatchers("/apply/save").permitAll()
                                .requestMatchers("/apply/**").permitAll()
                                .requestMatchers("/job/**").permitAll()
                                .requestMatchers("/contact/save").permitAll()
                                .requestMatchers("/savepopup").permitAll()
                                .requestMatchers("/internship").permitAll()
                                .requestMatchers("/popup").permitAll()
                                .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/blog/**").permitAll()
                                .requestMatchers("/realestate").permitAll()
                                .requestMatchers("/admin/postjob/**").hasAnyRole("ADMIN")
                                //.requestMatchers("/admin/contactList").hasRole("ADMIN")
                                //.requestMatchers("/admin/users").hasRole("ADMIN")
                                .requestMatchers("/admin/candidates").hasAnyRole("ADMIN")
                                .requestMatchers("/admin/contactList").hasAnyRole("ADMIN")
                                .requestMatchers("/admin/users").hasAnyRole("ADMIN")
                                .requestMatchers("/admin/uploads/**").hasAnyRole("ADMIN")
                                .requestMatchers("/users/delete").hasAnyRole("ADMIN")
                                .requestMatchers("/admin/users/export-csv/**").hasAnyRole("ADMIN")
                                .anyRequest().authenticated()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                //.defaultSuccessUrl("/admin/dashboard", true)
                                .successHandler(successHandler)
                                .failureHandler(failureHandler)
                                .permitAll()
                                
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }
  
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
