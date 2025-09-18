package com.example.datn.Controller;



import com.example.datn.Entity.DanhGia;

import com.example.datn.Entity.SanBong;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.LoaiMatSanRepo;
import com.example.datn.Repository.LoaiMonTheThaoRepo;
import com.example.datn.Repository.LoaiSanRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Security.CustomUserDetails;

import com.example.datn.Service.DanhGiaService;
import com.example.datn.Service.SanBongService;
import com.example.datn.Service.TaiKhoanService;

import org.springframework.security.crypto.password.PasswordEncoder;// mk

import com.example.datn.Service.ThongBaoService;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class SanBongController {

    private final SanBongService sanBongService;
    private final LoaiMatSanRepo loaiMatSanRepo;
    private final LoaiSanRepo loaiSanRepo;
    private final LoaiMonTheThaoRepo loaiMonTheThaoRepo;
    private final TaiKhoanService taiKhoanService;
    private final TaiKhoanRepo taiKhoanRepo;
    private final DanhGiaService danhGiaService;
    private final ThongBaoService thongBaoService;
    private final PasswordEncoder passwordEncoder;//mk

    public SanBongController(SanBongService sanBongService, LoaiMatSanRepo loaiMatSanRepo, LoaiSanRepo loaiSanRepo, LoaiMonTheThaoRepo loaiMonTheThaoRepo, TaiKhoanRepo taiKhoanRepo, TaiKhoanService taiKhoanService, TaiKhoanRepo taiKhoanRepo1, DanhGiaService danhGiaService, ThongBaoService thongBaoService,PasswordEncoder passwordEncoder) {

        this.sanBongService = sanBongService;
        this.loaiMatSanRepo = loaiMatSanRepo;
        this.loaiSanRepo = loaiSanRepo;
        this.loaiMonTheThaoRepo = loaiMonTheThaoRepo;
        this.taiKhoanService = taiKhoanService;
        this.taiKhoanRepo = taiKhoanRepo1;
        this.danhGiaService = danhGiaService;
        this.thongBaoService = thongBaoService;
        this.passwordEncoder = passwordEncoder; // mk
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/trang-chu";
    }

    // ✅ Trang chủ chính
    @GetMapping("/trang-chu")
    public String trangchu(Model model) {
        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);
        populateModel(model);
        return "Main/TrangChu";
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
        taiKhoan.setMat_khau(passwordEncoder.encode(password));//mk
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

    @GetMapping("/user/trang-chu")
    public String trangChu_nguoiDung(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());

        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);
        // Lấy thông tin tài khoản từ SecurityContext
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        TaiKhoan taiKhoan = userDetails.getTaiKhoan();

        //  Set idTaiKhoan vào session để sử dụng cho thông báo
        request.getSession().setAttribute("idTaiKhoan", taiKhoan.getId());
        danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);
        //
        int soLuongThongBaoMoi = thongBaoService.demThongBaoChuaDoc(taiKhoan.getId());
        model.addAttribute("soLuongThongBaoMoi", soLuongThongBaoMoi);

        // Gửi map danh sách đánh giá từng sân
        Map<Integer, List<DanhGia>> mapDanhGia = new HashMap<>();
        for (SanBong san : danhSachSan) {
            List<DanhGia> danhGias = danhGiaService.layDanhGiaTheoSan(san.getId_san_bong());
            mapDanhGia.put(san.getId_san_bong(), danhGias);
        }
        model.addAttribute("mapDanhGia", mapDanhGia);
        model.addAttribute("danhGiaMoi", new DanhGia()); // để gửi đánh giá mới
        model.addAttribute("hoTen", taiKhoanService.getHoTenDangNhap());

        populateModel(model);
        return "/Main/TrangChu_NguoiDung";
    }

//    @GetMapping("/thong-tin-ca-nhan")
//    public String thongTinCaNhan(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        var taiKhoanOpt = taiKhoanRepo.findByEmail(email);
//        if (taiKhoanOpt.isEmpty()) {
//            return "redirect:/login";
//        }
//        TaiKhoan taiKhoan = taiKhoanOpt.get();
//        model.addAttribute("taiKhoan", taiKhoan);
//        return "/Main/ThongTinCaNhan";
//    }

    @GetMapping("/admin/trang-chu")
    public String trangChu_QuanTri(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);
        populateModel(model);
        System.out.println("Logged-in user: " + auth.getName());
        return "Main/TrangChu_QuanTri";
    }

    @GetMapping("/ve-trang-chu")
    public String veTrangChuTheoRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {

            if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_QUAN_TRI"))) {
                return "redirect:/admin/trang-chu";
            } else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_NGUOI_DUNG"))) {
                return "redirect:/user/trang-chu";
            }
        }

        return "redirect:/trang-chu"; // Người chưa đăng nhập
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
                          @RequestParam(required = false) Integer loaiSan,
                          @RequestParam(required = false) Integer monTheThao,
                          @RequestParam(required = false) Integer trangThai) {

        if (keyword != null) {
/*
            keyword = keyword.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
*/
            keyword = keyword.trim();

            if (keyword.isEmpty()) {
                keyword = null;
            }
        }
        List<SanBong> ketQua = sanBongService.timKiemSan(keyword, loaiSan, monTheThao);
        List<SanBong> sanBongs = sanBongService.findAll();
        model.addAttribute("sanBongs", sanBongs);
        model.addAttribute("danhSachSan", ketQua);
        model.addAttribute("khongCoKetQua", ketQua.isEmpty());
        populateModel(model);
        return "/Main/TimKiem";
    }

    //    @GetMapping("/chi-tiet/{id}")
//    public String chiTietSan(@PathVariable("id") int id, Model model) {
//        SanBong san = sanBongService.findById(id);
//        if (san == null) {
//            return "redirect:/trang-chu"; // Chuyển hướng về trang chủ nếu không tìm thấy
//        }
//        model.addAttribute("sanBongChiTiet", san);
//        return "Main/ChiTietSan"; // 👉 trang riêng biệt
//    }

    @GetMapping("/chi-tiet/{id}")
    public String chiTietSan(@PathVariable("id") int id, Model model) {
        SanBong san = sanBongService.findById(id);
        if (san == null) {
            return "redirect:/trang-chu"; // Không tìm thấy sân thì về trang chủ
        }

        model.addAttribute("sanBongChiTiet", san);
        model.addAttribute("danhSachDanhGia", danhGiaService.layDanhGiaTheoSan(id));
        model.addAttribute("danhGia", new DanhGia()); // form đánh giá cần dòng này

        return "Main/ChiTietSan";
    }

    @GetMapping("/quan-ly-san")
    public String QuanLySan(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {

        Page<SanBong> danhSachSan = sanBongService.getSanBongPage(page, size);
        model.addAttribute("danhSachSan", danhSachSan);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", danhSachSan.getTotalPages());
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
        model.addAttribute("isTimKiem", false);

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        return "san/QuanLySan";
    }

    @GetMapping("/form-them-san-bong")
    public String formThem(Model model) {
        model.addAttribute("sanBong", new SanBong());
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsLoaiMonTheThao", loaiMonTheThaoRepo.findAll());
        model.addAttribute("dsLoaiMatSan", loaiMatSanRepo.findAll());
        return "san/ThemSan";
    }

    @PostMapping("/them-san-bong")
    public String themSanBong(@Valid @ModelAttribute("sanBong") SanBong sanBong, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws IOException {

        MultipartFile file = sanBong.getFile();


        if (file == null || file.isEmpty()) {
            bindingResult.rejectValue("file", "file.empty", "Bạn phải chọn file ảnh");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
            model.addAttribute("dsLoaiMonTheThao", loaiMonTheThaoRepo.findAll());
            model.addAttribute("dsLoaiMatSan", loaiMatSanRepo.findAll());
            return "san/ThemSan";
        }


        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            sanBong.setHinh_anh(fileName); // Chỉ lưu tên file
        }

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        // Lấy tài khoản đang đăng nhập
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        TaiKhoan taiKhoan = userDetails.getTaiKhoan();

        // Gán tài khoản vào sân bóng
        sanBong.setTaiKhoan(taiKhoan);
        try {
            sanBongService.them(sanBong);
            redirectAttributes.addFlashAttribute("success", "Thêm sân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm sân thất bại!");
        }

        return "redirect:/quan-ly-san";
    }

    @GetMapping("/xoa-san-bong/{id}")
    public String xoa(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            sanBongService.xoa(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa sân thất bại!");
        }
        return "redirect:/quan-ly-san";
    }

    @GetMapping("/sua-san-bong/{id}")
    public String viewSua(@PathVariable("id") int id, Model model) {

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        model.addAttribute("sanBong", sanBongService.findById(id));
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsLoaiMonTheThao", loaiMonTheThaoRepo.findAll());
        model.addAttribute("dsLoaiMatSan", loaiMatSanRepo.findAll());
        return "san/SuaSan";
    }

    @PostMapping("/sua-san-bong/{id}")
    public String suaSanBong(@Valid @ModelAttribute("sanBong") SanBong sanBong,
                             BindingResult bindingResult,
                             @PathVariable("id") int id,
                             Model model, RedirectAttributes redirectAttributes) throws IOException {

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);
        SanBong sanBongGoc = sanBongService.findById(id);
        if (sanBongGoc == null) {
            return "redirect:/quan-ly-san";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("dsLoaiMonTheThao", loaiMonTheThaoRepo.findAll());
            model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
            model.addAttribute("dsLoaiMatSan", loaiMatSanRepo.findAll());
            return "san/SuaSan";
        }

        // Cập nhật các trường thông tin khác
        sanBongGoc.setTen_san_bong(sanBong.getTen_san_bong());
        sanBongGoc.setMo_ta(sanBong.getMo_ta());
        sanBongGoc.setTrang_thai(sanBong.getTrang_thai());
        sanBongGoc.setLoaiMatSan(sanBong.getLoaiMatSan());
        sanBongGoc.setLoaiMonTheThao(sanBong.getLoaiMonTheThao());
        sanBongGoc.setLoaiSan(sanBong.getLoaiSan());

        MultipartFile file = sanBong.getFile();
        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            sanBongGoc.setHinh_anh(fileName); // Chỉ lưu tên file
        }

        try {
            sanBongService.sua(sanBongGoc);
            redirectAttributes.addFlashAttribute("success", "Sửa sân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Sửa sân thất bại!");
        }

        return "redirect:/quan-ly-san";
    }

    @GetMapping("/quan-ly-san/tim-kiem")
    public String quanLySanTimKiem(Model model,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(required = false) Integer loaiSan,
                                   @RequestParam(required = false) Integer monTheThao,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        if (keyword != null) {
            keyword = keyword.trim();
        }
        Page<SanBong> ketQua = sanBongService.timKiemTheoPage(keyword, loaiSan, monTheThao, page, size);
        model.addAttribute("danhSachSan", ketQua);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", ketQua.getTotalPages());
        model.addAttribute("khongCoKetQua", ketQua.isEmpty());

        model.addAttribute("keyword", keyword);
        model.addAttribute("loaiSan", loaiSan);
        model.addAttribute("monTheThao", monTheThao);

        model.addAttribute("isTimKiem", true);
        populateModel(model);
        return "san/QuanLySan";
    }

    private void populateModel(Model model) {
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
        model.addAttribute("dsLoaiMatSan", loaiMatSanRepo.findAll());
    }

}
