package com.example.datn.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "tai_khoan_ngan_hang")
public class TaiKhoanNganHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tai_khoan_ngan_hang")
    private Integer id;

    @Column(name = "so_tai_khoan", nullable = false)
    private String soTaiKhoan;

    @Column(name = "ten_ngan_hang", nullable = false,  columnDefinition = "NVARCHAR(255)")
    private String tenNganHang;

    @Column(name = "chu_tai_khoan", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String chuTaiKhoan;

    @Column(name = "bank_code", length = 50)
    private String bankCode;

}
