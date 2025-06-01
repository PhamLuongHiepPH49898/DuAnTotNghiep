package com.example.datn.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            String role = auth.getAuthority();
            System.out.println("ROLE: " + role);

            if (role.equals("ROLE_QUAN_TRI")) {
                response.sendRedirect("/admin/trang-chu");
                return;
            } else if (role.equals("ROLE_NGUOI_DUNG")) {
                response.sendRedirect("/user/trang-chu");
                return;
            }
        }

        // Mặc định nếu không khớp vai trò nào
        response.sendRedirect("/");
    }
}