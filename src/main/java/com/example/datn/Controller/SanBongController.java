package com.example.datn.Controller;

import com.example.datn.Entity.SanBong;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.LoaiMatSanRepo;
import com.example.datn.Repository.LoaiMonTheThaoRepo;
import com.example.datn.Repository.LoaiSanRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.SanBongService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SanBongController {
    private final SanBongService sanBongService;
    private final LoaiMatSanRepo loaiMatSanRepo;
    private final LoaiSanRepo loaiSanRepo;
    private final LoaiMonTheThaoRepo loaiMonTheThaoRepo;
    private final TaiKhoanRepo taiKhoanRepo;
    private final PasswordEncoder passwordEncoder;

    public SanBongController(SanBongService sanBongService, LoaiMatSanRepo loaiMatSanRepo,
                             LoaiSanRepo loaiSanRepo, LoaiMonTheThaoRepo loaiMonTheThaoRepo,
                             TaiKhoanRepo taiKhoanRepo,PasswordEncoder passwordEncoder) {
        this.sanBongService = sanBongService;
        this.loaiMatSanRepo = loaiMatSanRepo;
        this.loaiSanRepo = loaiSanRepo;
        this.loaiMonTheThaoRepo = loaiMonTheThaoRepo;
        this.taiKhoanRepo = taiKhoanRepo;
        this.passwordEncoder = passwordEncoder;

    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/trangchu";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu sai");
        }
        return "Main/Login";
    }

    @GetMapping("/dang-ky")
    public String dangKy() {
        return "Main/DangKi";
    }

    @PostMapping("/dang-ky")
    public String processDangKy(@RequestParam String fullname,
                                @RequestParam String email,
                                @RequestParam String phone,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                Model model) {

        boolean hasErrors = false;

        if (fullname == null || fullname.trim().isEmpty() || !fullname.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
            model.addAttribute("errorFullname", "Họ tên không hợp lệ, chỉ gồm chữ và khoảng trắng.");
            hasErrors = true;
        }

        if (email == null || !email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            model.addAttribute("errorEmail", "Email không hợp lệ.");
            hasErrors = true;
        } else if (taiKhoanRepo.findByEmail(email).isPresent()) {
            model.addAttribute("errorEmail", "Email đã tồn tại!");
            hasErrors = true;
        }

        if (phone == null || !phone.matches("^0\\d{9,10}$")) {
            model.addAttribute("errorPhone", "Số điện thoại không hợp lệ (bắt đầu bằng 0, 10-11 số).");
            hasErrors = true;
        } else if (taiKhoanRepo.findBySdt(phone).isPresent()) {
            model.addAttribute("errorPhone", "Số điện thoại đã tồn tại!");
            hasErrors = true;
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorPassword", "Mật khẩu nhập lại không khớp!");
            hasErrors = true;
        }

        if (hasErrors) {
            model.addAttribute("fullname", fullname);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            return "Main/DangKi";
        }

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setHo_ten(fullname);
        taiKhoan.setEmail(email);
        taiKhoan.setSo_dien_thoai(phone);
        taiKhoan.setMat_khau(passwordEncoder.encode(password));
        taiKhoan.setVai_tro("NGUOI_DUNG");
        taiKhoan.setTrang_thai(0); // Tài khoản hoạt động

        taiKhoanRepo.save(taiKhoan);
        return "redirect:/user/trang-chu";
    }

    @GetMapping("/doi-mat-khau")
    public String showFormDoiMK() {
        return "/Main/DoiPass";
    }

    @PostMapping("/doi-mat-khau")
    public String doiMK(@RequestParam("mkCu") String mkCu,
                        @RequestParam("mkMoi") String mkMoi,
                        @RequestParam("xacnhanMK") String xacnhanMK,
                        Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        TaiKhoan taiKhoan = taiKhoanRepo.findByEmail(email).orElse(null);

        if (taiKhoan == null) {
            model.addAttribute("error", "Không tìm thấy tài khoản.");
            return "/Main/DoiPass";
        }

        if (!passwordEncoder.matches(mkCu, taiKhoan.getMat_khau())) {
            model.addAttribute("error", "Mật khẩu cũ không đúng.");
            return "/Main/DoiPass";
        }

        if (!mkMoi.equals(xacnhanMK)) {
            model.addAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
            return "/Main/DoiPass";
        }


        // Lưu mật khẩu mới sau khi mã hóa
        taiKhoan.setMat_khau(passwordEncoder.encode(mkMoi));
        taiKhoanRepo.save(taiKhoan);

        model.addAttribute("message", "Đổi mật khẩu thành công!");
        return "/Main/DoiPass";
    }


    @GetMapping("/logout")
    public String logoutPage(Model model) {
        return "Main/TrangChu";
    }

    @GetMapping("/trangchu")
    public String trangchu(Model model) {
        return "/Main/TrangChu";
    }

    @GetMapping("/user/trang-chu")
    public String trangChu_nguoiDung(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);
        populateModel(model);
        return "/Main/TrangChu_NguoiDung";
    }

    @GetMapping("/thong-tin-ca-nhan")
    public String thongTinCaNhan(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        var taiKhoanOpt = taiKhoanRepo.findByEmail(email);
        if (taiKhoanOpt.isEmpty()) {
            return "redirect:/login";
        }
        TaiKhoan taiKhoan = taiKhoanOpt.get();
        model.addAttribute("taiKhoan", taiKhoan);
        return "/Main/ThongTinCaNhan";
    }

    @GetMapping("/admin/trang-chu")
    public String trangChu_QuanTri(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);
        populateModel(model);
        return "/Main/TrangChu_QuanTri";
    }

    @GetMapping("/danh-sach-user")
    public String dsUser(Model model) {
        List<TaiKhoan> danhSach = taiKhoanRepo.findByVaiTro("NGUOI_DUNG");
        model.addAttribute("danhSachNguoiDung", danhSach);
        return "/Main/DanhSachNguoiDung";
    }

    @GetMapping("/tim-kiem")
    public String timKiem(Model model,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(required = false) Long loaiSan,
                          @RequestParam(required = false) Long monTheThao) {
        if (keyword != null) {
            keyword = keyword.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
        }
        List<SanBong> ketQua = sanBongService.timKiemSan(keyword, loaiSan, monTheThao);
        model.addAttribute("danhSachSan", ketQua);
        model.addAttribute("khongCoKetQua", ketQua.isEmpty());
        populateModel(model);
        return "/Main/TimKiem";
    }

    @GetMapping("/chi-tiet/{id}")
    public String chiTietSan(@PathVariable("id") int id, Model model) {
        SanBong san = sanBongService.findById(id);
        if (san == null) {
            return "redirect:/trangchu";
        }
        model.addAttribute("sanBongChiTiet", san);
        return "/Main/ChiTietSan";
    }

    private void populateModel(Model model) {
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
    }
}
