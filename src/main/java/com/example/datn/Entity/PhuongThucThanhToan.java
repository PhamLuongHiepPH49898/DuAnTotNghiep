package com.example.datn.Entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "phuong_thuc_thanh_toan")
public class PhuongThucThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phuong_thuc_thanh_toan")
    private int idPhuongThucThanhToan;

    @Column(name = "ten_phuong_thuc_thanh_toan", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String tenPhuongThucThanhToan;

    @Column(name = "trang_thai", nullable = false)
    private int trangThai;
}
