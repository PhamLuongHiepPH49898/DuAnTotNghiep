package com.example.datn.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tai_khoan")
    private int id;

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "mat_khau")
    private String password;

    @Column(name = "so_dien_thoai")
    private String sdt;

    @Column(name = "vai_tro")
    private String vaiTro;

    @Column(name = "trang_thai")
    private int trangThai;

    @OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SanBong> dsSanBong;
}
