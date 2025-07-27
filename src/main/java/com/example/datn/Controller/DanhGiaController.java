package com.example.datn.Controller;

import com.example.datn.Entity.DanhGia;
import com.example.datn.Entity.SanBong;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Service.DanhGiaService;
import com.example.datn.Service.SanBongService;
import com.example.datn.Service.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/danh-gia")
public class DanhGiaController {
    @Autowired
    private DanhGiaService danhGiaService;

    @Autowired
    private SanBongService sanBongService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    // ✅ Gửi đánh giá từ modal
    @PostMapping("/them")
    public String themDanhGia(@ModelAttribute DanhGia danhGia,
                              @RequestParam("idSan") Integer idSan) {
        SanBong san = sanBongService.findById(idSan);
        if (san == null) return "redirect:/error";

        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanDangNhap();
        if (taiKhoan == null) return "redirect:/login";

        danhGia.setSanBong(san);
        danhGia.setTaiKhoan(taiKhoan);
        danhGia.setTrangThai(1); // hiển thị
        danhGiaService.luuDanhGia(danhGia);

        return "redirect:/chi-tiet/" + idSan;
    }


    // ✅ Xem danh sách đánh giá của sân (nếu cần dùng)
    @GetMapping("/san/{idSan}")
    public String hienThiDanhSachDanhGia(@PathVariable("idSan") Integer idSan, Model model) {
        model.addAttribute("danhSachDanhGia", danhGiaService.layDanhGiaTheoSan(idSan));
        return "danh-gia/danh-sach"; // hoặc nhúng vào chi-tiet.html
    }
}
