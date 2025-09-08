package com.example.datn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "danh_gia")
public class DanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_danh_gia")
    private Integer id;
    @Column(name = "so_sao")
    private Integer sao;
    @Column(name = "noi_dung")
    private String noiDung;
    @Column(name = "ngay_danh_gia")
    private LocalDate ngayDanhGia;
    @Column(name = "trang_thai")
    private Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan taiKhoan;

    @ManyToOne
    @JoinColumn(name = "id_san_bong")
    private SanBong sanBong;
//
//    @ManyToOne
//    @JoinColumn(name = "id_lich_dat_san")
//    private LichDatSan lichDatSan;


}

