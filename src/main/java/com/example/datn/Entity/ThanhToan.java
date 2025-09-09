package com.example.datn.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "lichDatSan", "taiKhoan"})
@Table(name = "thanh_toan")
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thanh_toan")
    private int idThanhToan;

    @Column(name = "so_tien", nullable = false)
    private double soTien;

    @Column(name = "ngay_thanh_toan", nullable = false)
    private LocalDateTime ngayThanhToan = LocalDateTime.now();

    @Column(name = "trang_thai", nullable = false)
    private int trangThai = 0;

    @Column(name = "han_thanh_toan", nullable = false)
    private LocalDateTime hanThanhToan;

    @Column(name = "reference")
    private String reference;


    @ManyToOne
    @JoinColumn(name = "id_tai_khoan", nullable = false)
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy = "thanhToan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichDatSan> lichDatSans = new ArrayList<>();
}
