package com.example.datn.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="gia_theo_khung_gio")
public class GiaTheoKhungGio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gia_theo_khung_gio")
    private int idGiaTheoKhungGio;

    @Column(name = "gia_thue", nullable = false)
    private BigDecimal giaThue;

    @Column(name = "trang_thai", nullable = false)
    private Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "id_san_bong")
    private SanBong sanBong;

    @ManyToOne
    @JoinColumn(name = "id_khung_gio")
    private KhungGio khungGio;
}
