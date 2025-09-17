package com.example.datn.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hoan_tien")
public class HoanTien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "so_tien_hoan", nullable = false)
    private BigDecimal soTienHoan;

    @Column(name = "so_tien_da_thanh_toan", nullable = false)
    private BigDecimal soTienDaThanhToan;

    @Column(name = "phan_tram_hoan", nullable = false)
    private BigDecimal phanTramHoan;

    @Column(name = "ly_do", columnDefinition = "NVARCHAR(255)")
    private String lyDo;

    @Column(name = "trang_thai", nullable = false)
    private Integer trangThai; // 0: chờ xử lý, 1: đã hoàn, 2: từ chối

    @Column(name = "ngay_huy", nullable = false)
    private LocalDateTime ngayHuy;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "id_lich_dat_san", nullable = false)
    private LichDatSan lichDatSan;
}
