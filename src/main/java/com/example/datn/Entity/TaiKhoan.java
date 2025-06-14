package com.example.datn.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tai_khoan" )
    private int id;

    @Column(name = "ho_ten", columnDefinition = "NVARCHAR(255)")
    private String ho_ten;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "mat_khau")
    private String mat_khau;

    @Column(name = "so_dien_thoai")
    private String so_dien_thoai;

    @Column(name = "vai_tro")
    private String vai_tro;

    @Column(name = "trang_thai")
    private int trang_thai;

    @OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SanBong> dsSanBong;

}
