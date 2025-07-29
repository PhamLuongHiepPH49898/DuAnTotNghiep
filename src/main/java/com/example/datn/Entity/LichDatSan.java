package com.example.datn.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "lich_dat_san")
public class LichDatSan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lich_dat_san")
    private int id;

    @Column(name = "ngay_dat", nullable = false)
    private LocalDate ngayDat;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDate ngayTao = LocalDate.now();

    @Column(name = "gia_ap_dung", nullable = false)
    private BigDecimal giaApDung;

    @Column(name = "ghi_chu", columnDefinition = "NVARCHAR(255)")
    private String ghiChu;

    @Column(name = "trang_thai", nullable = false)
    private Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan taiKhoan;


    @ManyToOne
    @JoinColumn(name = "id_san_bong")
    private SanBong sanBong;

    @ManyToOne
    @JoinColumn(name = "id_gia_theo_khung_gio")
    private GiaTheoKhungGio giaTheoKhungGio;

}
