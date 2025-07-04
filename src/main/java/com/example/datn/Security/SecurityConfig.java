package com.example.datn.Security;

import com.example.datn.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    @Autowired
    private CustomSuccessHandler customSuccessHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/trangchu", "/css/**", "/js/**", "/image/**").permitAll() // Cho phép truy cập công cộng
                        .requestMatchers("/admin/**").hasRole("QUAN_TRI")
                        .requestMatchers("/user/**").hasRole("NGUOI_DUNG")
                        .requestMatchers("/login", "/dang-ky").permitAll() // Trang login và register công khai
                        .anyRequest().authenticated() // Các yêu cầu khác phải được xác thực
                )
                .formLogin(form -> form
                        .loginPage("/login")                  // Trang đăng nhập tùy chỉnh
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(customSuccessHandler)
                        .failureUrl("/login?error")            // Khi đăng nhập sai sẽ quay về /login với param error
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL đăng xuất
                        .logoutSuccessUrl("/trangchu") // Chuyển hướng sau khi đăng xuất
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authManagerBuilder.build();
    }
}
