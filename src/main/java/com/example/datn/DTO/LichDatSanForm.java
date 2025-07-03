package com.example.datn.DTO;

import com.example.datn.Entity.LichDatSan;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
public class LichDatSanForm {
//    @NotNull
//    private LocalDate ngayDat;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")   // ✅ thêm dòng này
    private LocalDate ngayDat;
    @NotNull
    private Integer idGiaTheoKhungGio;

    private String ghiChu;

    public static LichDatSanForm fromEntity(LichDatSan e) {
        LichDatSanForm f = new LichDatSanForm();
        f.setNgayDat(e.getNgayDat());
        f.setIdGiaTheoKhungGio(e.getGiaTheoKhungGio().getIdGiaTheoKhungGio());
        f.setGhiChu(e.getGhiChu());          // copy ghi chú
        return f;
    }
}
