package com.example.datn.Security;

import com.example.datn.Entity.TaiKhoan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final TaiKhoan taiKhoan;

    public CustomUserDetails(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+taiKhoan.getVai_tro()));
    }
    @Override
    public String getPassword(){
        return taiKhoan.getMat_khau();
    }
    @Override
    public String getUsername(){
        return taiKhoan.getEmail();
    }
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isEnabled() {
        return taiKhoan.getTrang_thai() == 0;
    }
    public String getPassword1() {
        System.out.println("Mật khẩu trả về: " + taiKhoan.getMat_khau());
        return taiKhoan.getMat_khau();
    }
    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public String getHoTen() {
        return taiKhoan.getHo_ten();
    }

}
