package com.example.datn.Controller;

import com.example.datn.Entity.*;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.PhuongThucThanhToanRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Repository.ThanhToanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class GiaLapThanhToanController {

    @Autowired
    private PhuongThucThanhToanRepo phuongThucRepo;

    @Autowired
    private ThanhToanRepo thanhToanRepo;

    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    @Autowired
    private TaiKhoanRepo taiKhoanRepo;

    @GetMapping("/gia-lap-thanh-toan")
    public String hienThiForm(@RequestParam("idLichDatSan") Integer idLich, Model model) {
        model.addAttribute("dsPhuongThuc", phuongThucRepo.findAll());
        model.addAttribute("idLichDatSan", idLich);
        return "Main/GiaLapThanhToan";
    }

    @PostMapping("/gia-lap-thanh-toan")
    public String thucHienThanhToan(@RequestParam("idLichDatSan") Integer idLich,
                                    @RequestParam("idPhuongThucThanhToan") Integer idPT,
                                    Principal principal) {

        // Lấy thông tin người dùng
        Optional<TaiKhoan> taiKhoan = taiKhoanRepo.findByEmail(principal.getName());
        Optional<LichDatSan> lichDatSan = lichDatSanRepo.findById(idLich);
        Optional<PhuongThucThanhToan> phuongThuc = phuongThucRepo.findById(idPT);

        if (taiKhoan.isPresent() && lichDatSan.isPresent() && phuongThuc.isPresent()) {
            GiaTheoKhungGio gia = lichDatSan.get().getGiaTheoKhungGio();

            ThanhToan thanhToan = new ThanhToan();
            thanhToan.setNgayThanhToan(LocalDate.now());
            thanhToan.setSoTien(gia.getGiaThue());
            thanhToan.setTrangThai(1); // 1: Đã thanh toán
            thanhToan.setTaiKhoan(taiKhoan.get());
            thanhToan.setLichDatSan(lichDatSan.get());
            thanhToan.setPhuongThucThanhToan(phuongThuc.get());

            thanhToanRepo.save(thanhToan);
        }

        return "redirect:/datLichThanhCong";
    }
}

