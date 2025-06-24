package com.example.datn.Controller;

import com.example.datn.Entity.LichDatSan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.ThongTinNguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class ThongTinNguoiDungController {

    @Autowired
    private TaiKhoanRepo taiKhoanRepo;

    @Autowired
    private ThongTinNguoiDungService thongTinNguoiDungService;

    @GetMapping("/thong-tin-nguoi-dung")
    public String hienThiThongTinNguoiDung(Model model,
                                           Principal principal,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(required = false) String keyword) {
        String email = principal.getName();
        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepo.findByEmail(email);
        if (optionalTaiKhoan.isEmpty()) {
            return "redirect:/login?error=notfound";
        }

        TaiKhoan taiKhoan = optionalTaiKhoan.get();
        Pageable pageable = PageRequest.of(page, 6, Sort.by("ngayDat").descending());

        Page<LichDatSan> lichSuPage;

        // Nếu có từ khóa tìm kiếm thì tìm theo tên sân
        if (keyword != null && !keyword.trim().isEmpty()) {
            lichSuPage = thongTinNguoiDungService.timKiemLichSuDatSanTheoTenSan(
                    (long) taiKhoan.getId(), keyword.trim(), pageable);
            model.addAttribute("keyword", keyword); // để giữ giá trị trong ô tìm kiếm
        } else {
            lichSuPage = thongTinNguoiDungService.layLichSuDatSan((long) taiKhoan.getId(), pageable);
        }

        model.addAttribute("taiKhoan", taiKhoan);
        model.addAttribute("lichSuPage", lichSuPage);
        model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return "ThongTinND/TTND";
    }

}
