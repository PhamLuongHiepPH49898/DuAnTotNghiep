package com.example.datn.Controller;


import com.example.datn.Entity.KhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.ThongBao;
import com.example.datn.Repository.KhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Service.ThongBaoService;
import com.example.datn.Uttil.EnvUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/thong-bao")
public class ThongBaoController {

    @Autowired
    private ThongBaoService thongBaoService;

    @Autowired
    private LichDatSanRepo lichDatSanRepository;

    @Autowired
    private KhungGioRepo khungGioRepository;


    private String hienThiThongBao(Model model, HttpSession session) {
        Integer idTaiKhoan = (Integer) session.getAttribute("idTaiKhoan");
        System.out.println("[DEBUG] idTaiKhoan trong session = " + idTaiKhoan);
        System.out.println(EnvUtil.get("MAIL_USERNAME"));
        System.out.println(EnvUtil.get("MAIL_PASSWORD"));

        if (idTaiKhoan == null) {
            model.addAttribute("thongBaoList", List.of());
            return "ThongBao/ThongBao";
        }

        List<ThongBao> thongBaoList = thongBaoService.layThongBaoTheoTaiKhoan(idTaiKhoan);

        model.addAttribute("thongBaoList", thongBaoList);
        return "ThongBao/ThongBao";
    }
    @GetMapping("/xem-thong-bao")
    public String hienThiThongBaoGET(Model model,
                                     HttpSession session,
                                     Authentication authentication) {
        setHomeUrl(model, authentication);
        return hienThiThongBao(model, session);
    }

    @PostMapping("/xem-thong-bao")
    public String hienThiThongBaoPOST(Model model,
                                      HttpSession session,
                                      Authentication authentication) {
        setHomeUrl(model, authentication);
        return hienThiThongBao(model, session);
    }

    // Hàm phụ để truyền homeUrl
    private void setHomeUrl(Model model, Authentication authentication) {
        String homeUrl = "/trang-chu"; // giá trị mặc định nếu không có role
        if (authentication != null) {
            boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_QUAN_TRI"));
            homeUrl = isAdmin ? "/admin/trang-chu" : "/user/trang-chu";
        }
        model.addAttribute("homeUrl", homeUrl);
    }

    @GetMapping("/chi-tiet/{idThongBao}")
    public String chiTietThongBao(@PathVariable Integer idThongBao, Model model) {
        // Cập nhật trạng thái thành "đã đọc"
        thongBaoService.danhDauThongBaoDaDoc(idThongBao);

        // Lấy thông báo để hiển thị chi tiết
        ThongBao thongBao = thongBaoService.layThongBaoTheoId(idThongBao)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));

        model.addAttribute("thongBao", thongBao);
        return "ThongBao/ChiTiet";  // Tạo file này để hiển thị chi tiết
    }



    // Gửi thông báo nhắc lịch
    @PostMapping("/nhac-lich/{idLichDatSan}")
    public String nhacLichDatSan(@PathVariable Integer idLichDatSan) {
        LichDatSan lichDatSan = lichDatSanRepository.findById(idLichDatSan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch đặt sân"));

        KhungGio khungGio = khungGioRepository.findById(lichDatSan.getGiaTheoKhungGio().getKhungGio().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khung giờ"));

        thongBaoService.taoThongBaoNhacLichTruoc1h(lichDatSan, khungGio);
        return "redirect:/thong-bao/xem-thong-bao";
    }
}
