package com.example.datn.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.time.LocalDate;


@Entity
@Table(name = "lich_dat_san")
public class LichDatSan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lich_dat_san")
    private int id;

    @Column(name = "ngay_dat", nullable = false)
    private LocalDate ngayDat;

    @Column(name = "trang_thai", nullable = false)
    private Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan taiKhoan;

    @ManyToOne
    @JoinColumn(name = "id_gia_theo_khung_gio")
    private GiaTheoKhungGio giaTheoKhungGio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDate ngayDat) {
        this.ngayDat = ngayDat;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public GiaTheoKhungGio getGiaTheoKhungGio() {
        return giaTheoKhungGio;
    }

    public void setGiaTheoKhungGio(GiaTheoKhungGio giaTheoKhungGio) {
        this.giaTheoKhungGio = giaTheoKhungGio;
    }
}
