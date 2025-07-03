package com.example.datn.Controller;


import com.example.datn.Entity.SanBong;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Entity.*;
import com.example.datn.Repository.LoaiMatSanRepo;
import com.example.datn.Repository.LoaiMonTheThaoRepo;
import com.example.datn.Repository.LoaiSanRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Security.CustomUserDetails;
import com.example.datn.Service.SanBongService;
import com.example.datn.Service.TaiKhoanService;
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
import com.example.datn.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Controller
public class SanBongController {

    private final SanBongService sanBongService;
    private final LoaiMatSanRepo loaiMatSanRepo;
    private final LoaiSanRepo loaiSanRepo;
    private final LoaiMonTheThaoRepo loaiMonTheThaoRepo;
    private final TaiKhoanService taiKhoanService;

    public SanBongController(SanBongService sanBongService, LoaiMatSanRepo loaiMatSanRepo, LoaiSanRepo loaiSanRepo, LoaiMonTheThaoRepo loaiMonTheThaoRepo, TaiKhoanRepo taiKhoanRepo, TaiKhoanService taiKhoanService) {
        this.sanBongService = sanBongService;
        this.loaiMatSanRepo = loaiMatSanRepo;
        this.loaiSanRepo = loaiSanRepo;
        this.loaiMonTheThaoRepo = loaiMonTheThaoRepo;
        this.taiKhoanService = taiKhoanService;
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

    @GetMapping("/logout")
    public String logoutPage(Model model) {
        return "Main/TrangChu";
    }

    @GetMapping("/dang-ky")
    public String dangKy() {
        return "Main/DangKi";
    }


    @GetMapping("/user/trang-chu")
    public String trangChu_nguoiDung(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Logged-in user: " + auth.getName());
        model.addAttribute("username", auth.getName());
        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);
        populateModel(model);
        System.out.println("Logged-in user: " + auth.getName());
        return "Main/TrangChu_NguoiDung";
    }

    @GetMapping("/admin/trang-chu")
    public String trangChu_QuanTri(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Logged-in user: " + auth.getName());
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


    // ✅ Trang tìm kiếm riêng
    @GetMapping("/tim-kiem")
    public String timKiem(Model model,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(required = false) Integer loaiSan,
                          @RequestParam(required = false) Integer monTheThao,
                          @RequestParam(required = false) Integer trangThai) {

        if (keyword != null) {
            keyword = keyword.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }
        List<SanBong> ketQua = sanBongService.timKiemSan(keyword, loaiSan, monTheThao);
        List<SanBong> sanBongs = sanBongService.findAll();
        model.addAttribute("sanBongs", sanBongs);
        model.addAttribute("danhSachSan", ketQua);
        model.addAttribute("khongCoKetQua", ketQua.isEmpty());
        populateModel(model); // Gợi ý: đảm bảo phương thức này nạp các danh sách như danh sách loại sân, môn thể thao v.v.
        return "Main/TimKiem";
    }

    @GetMapping("/chi-tiet/{id}")
    public String chiTietSan(@PathVariable("id") int id, Model model) {
        SanBong san = sanBongService.findById(id);
        if (san == null) {
            return "redirect:/trang-chu"; // Chuyển hướng về trang chủ nếu không tìm thấy
        }
        model.addAttribute("sanBongChiTiet", san);
        return "Main/ChiTietSan"; // 👉 trang riêng biệt
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
            keyword = keyword.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
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

    // Phương thức tiện ích để thêm các thuộc tính chung vào model
    private void populateModel(Model model) {
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
        model.addAttribute("dsLoaiMatSan", loaiMatSanRepo.findAll());
    }

}
