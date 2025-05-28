package com.example.datn.Security;

import com.example.datn.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/trangchu", "/css/**", "/js/**", "/image/**", "/login", "/register", "/error").permitAll() // ✅ Cho phép truy cập công khai
                        .requestMatchers("/admin/**").hasAuthority("QUAN_TRI") // ✅ Chỉ admin mới có quyền truy cập
                        .requestMatchers("/user/**").hasAuthority("NGUOI_DUNG") // ✅ Chỉ người dùng mới có quyền truy cập
                        .anyRequest().authenticated() // ✅ Các trang khác yêu cầu đăng nhập
                )
                .formLogin(form -> form
                        .loginPage("/login") // ✅ Trang đăng nhập tùy chỉnh
                        .defaultSuccessUrl("/trangchu", true) // ✅ Sau khi đăng nhập, chuyển đến trang chủ
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // ✅ URL đăng xuất
                        .logoutSuccessUrl("/trangchu") // ✅ Sau khi đăng xuất, quay về trang chủ
                        .invalidateHttpSession(true) // ✅ Hủy phiên đăng nhập
                        .deleteCookies("JSESSIONID") // ✅ Xóa cookie phiên làm việc
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // ⚠️ CSRF bị vô hiệu hóa (cân nhắc bật nếu cần)

        return http.build();
    }



    // Password encoder dùng BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình AuthenticationManager dùng UserDetailsService và passwordEncoder
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
