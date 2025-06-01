package com.example.datn.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "san_bong")
public class SanBong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_san_bong")
    private int id_san_bong;

    @NotBlank(message = "Tên sân bóng không được để trống")
    @Column(name = "ten_san_bong")
    private String ten_san_bong;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Column(name = "dia_chi")
    private String dia_chi;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải là số dương")
    @Column(name = "gia")
    private Double gia;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    @Column(name = "mo_ta")
    private String mo_ta;

    @Column(name = "hinh_anh")
    private String hinh_anh;

    @Column(name = "trang_thai")
    private int trang_thai;

    @NotNull(message = "Loại sân không được để trống")
    @ManyToOne
    @JoinColumn(name = "id_loai_san")
    private LoaiSan loaiSan;

    @NotNull(message = "Loại mặt sân không được để trống")
    @ManyToOne
    @JoinColumn(name = "id_loai_mat_san")
    private LoaiMatSan loaiMatSan;

    @NotNull(message = "Môn thể thao không được để trống")
    @ManyToOne
    @JoinColumn(name = "id_loai_mon_the_thao")
    private LoaiMonTheThao loaiMonTheThao;

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan taiKhoan;

    @Transient
    private MultipartFile file; // dùng để bind file upload, không lưu DB

    @Override
    public String toString() {
        return "SanBong{" +
               "id_san_bong=" + id_san_bong +
               ", ten_san_bong='" + ten_san_bong + '\'' +
               ", dia_chi='" + dia_chi + '\'' +
               ", gia=" + gia +
               ", mo_ta='" + mo_ta + '\'' +
               ", hinh_anh='" + hinh_anh + '\'' +
               ", trang_thai=" + trang_thai +
               ", loaiSan=" + loaiSan +
               ", loaiMatSan=" + loaiMatSan +
               ", loaiMonTheThao=" + (loaiMonTheThao != null ? loaiMonTheThao.getTen_mon_the_thao() : null) +
               ", taiKhoan=" + taiKhoan +
               '}';
    }


}
