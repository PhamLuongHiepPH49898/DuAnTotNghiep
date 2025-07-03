package com.example.datn.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "danh_gia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_danh_gia")
    private Integer id;

    @Column(name = "so_sao")
    private Integer sao;

    private String noiDung;
    private LocalDate ngayDanhGia;
    private Integer trangThai;

    @ManyToOne @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan taiKhoan;

    @ManyToOne @JoinColumn(name = "id_san_bong")
    private SanBong sanBong;

    @ManyToOne
    @JoinColumn(name = "id_lich_dat_san")
    private LichDatSan lichDatSan;
}
