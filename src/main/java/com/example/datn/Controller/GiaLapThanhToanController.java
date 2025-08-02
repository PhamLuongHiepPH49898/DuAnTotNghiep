package com.example.datn.Controller;

import com.example.datn.DTO.XacNhanDatLichDTO;
import com.example.datn.Entity.*;
import com.example.datn.Repository.*;
import com.example.datn.Service.DatSanService;
import com.example.datn.Service.XacNhanDatLichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class GiaLapThanhToanController {

    @Autowired
    private PhuongThucThanhToanRepo phuongThucRepo;

    @Autowired
    private TaiKhoanNganHangRepository taiKhoanNganHangRepo;

    @Autowired
    private ThanhToanRepo thanhToanRepo;

    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    @Autowired
    private TaiKhoanRepo taiKhoanRepo;

    @Autowired
    private XacNhanDatLichService xacNhanDatLichService;

    @Autowired
    private DatSanService datSanService;


    @GetMapping("/gia-lap-thanh-toan")
    public String hienThiForm(@RequestParam("idLichDatSan") Integer idLich,
                              Model model,
                              Principal principal) {
        // Lấy tài khoản đang đăng nhập
        String email = principal.getName();
        TaiKhoan taiKhoan = taiKhoanRepo.findByEmail(email).orElse(null);

        List<PhuongThucThanhToan> danhSachPhuongThuc = phuongThucRepo.findAll();
        List<TaiKhoanNganHang> danhSachTaiKhoan = taiKhoanNganHangRepo.findAll();

        model.addAttribute("dsPhuongThuc", danhSachPhuongThuc);
        model.addAttribute("dsTaiKhoanNganHang", danhSachTaiKhoan);
        model.addAttribute("idLichDatSan", idLich);

        if (taiKhoan != null) {
            model.addAttribute("idTaiKhoan", taiKhoan.getId()); // ✅ truyền vào view
        }

        return "Main/GiaLapThanhToan";
    }


    @PostMapping("/gia-lap-thanh-toan")
    public String thanhToanVaLuu(@RequestParam("idLichDatSan") int idLichDatSan,
                                 @RequestParam("idPhuongThucThanhToan") int idPhuongThuc,
                                 Model model) {
        // Cập nhật trạng thái thanh toán
        xacNhanDatLichService.capNhatTrangThaiThanhToan(idLichDatSan, true);

        // (Tùy chọn) Nếu bạn muốn lưu phương thức thanh toán, thêm code tại đây

        return "redirect:/datLichThanhCong";
    }


    @ResponseBody
    @GetMapping("/tai-khoan-ngan-hang-theo-phuong-thuc")
    public List<TaiKhoanNganHang> layTaiKhoanTheoPhuongThuc(@RequestParam("idPhuongThuc") Integer idPhuongThuc) {
        return taiKhoanNganHangRepo.findByPhuongThuc_IdPhuongThucThanhToan(idPhuongThuc);
    }
}
