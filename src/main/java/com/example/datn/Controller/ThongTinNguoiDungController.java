package com.example.datn.Controller;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.ThongTinNguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if (keyword != null && !keyword.trim().isEmpty()) {
            lichSuPage = thongTinNguoiDungService.timKiemLichSuDatSanTheoTenSan(
                    (long) taiKhoan.getId(), keyword.trim(), pageable);
            model.addAttribute("keyword", keyword);
        } else {
            lichSuPage = thongTinNguoiDungService.layLichSuDatSan((long) taiKhoan.getId(), pageable);
        }

        List<LichDatSan> lichHomNay = thongTinNguoiDungService.timLichDatHomNay((long) taiKhoan.getId());
        boolean coLichHomNay = lichHomNay.stream().anyMatch(l -> l.getTrangThai() == 1);
        model.addAttribute("lichChoXacNhan", lichSuPage.getContent().stream()
                .filter(l -> l.getTrangThai() == 0).collect(Collectors.toList()));

        model.addAttribute("lichDaXacNhan", lichSuPage.getContent().stream()
                .filter(l -> l.getTrangThai() == 1).collect(Collectors.toList()));

        model.addAttribute("lichDaHuy", lichSuPage.getContent().stream()
                .filter(l -> l.getTrangThai() == 2).collect(Collectors.toList()));
        model.addAttribute("coLichHomNay", coLichHomNay);
        model.addAttribute("taiKhoan", taiKhoan);
        model.addAttribute("lichSuPage", lichSuPage);
        model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return "ThongTinND/TTND";
    }
}
