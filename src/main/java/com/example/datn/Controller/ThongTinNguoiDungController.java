package com.example.datn.Controller;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.ThongTinNguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ThongTinNguoiDungController {

    @Autowired
    private TaiKhoanRepo taiKhoanRepo;

    @Autowired
    private ThongTinNguoiDungService thongTinNguoiDungService;

    @GetMapping("/thong-tin-nguoi-dung")
    public String hienThiThongTinNguoiDung(Model model, Principal principal) {
        String email = principal.getName();

        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepo.findByEmail(email);
        if (optionalTaiKhoan.isEmpty()) {
            return "redirect:/login?error=notfound";
        }

        TaiKhoan taiKhoan = optionalTaiKhoan.get();
        List<LichDatSan> lichSuDatSan = thongTinNguoiDungService.layLichSuDatSan(taiKhoan.getId());

        model.addAttribute("taiKhoan", taiKhoan);
        model.addAttribute("lichSuDatSan", lichSuDatSan);

        return "ThongTinND/TTND";
    }
}
