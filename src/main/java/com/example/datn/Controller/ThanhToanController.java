package com.example.datn.Controller;

import com.example.datn.Entity.ThanhToan;
import com.example.datn.Service.ThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ThanhToanController {

    @Autowired
    private ThanhToanService thanhToanService;

    /**
     * Trang thanh toán (có cả trạng thái + đếm ngược)
     */
    @GetMapping("/thanh-toan")
    public String hienThiTrangThanhToan(@RequestParam("idLichDatSan") Integer idLichDatSan, Model model) {
        ThanhToan tt = thanhToanService.taoGiaoDichChoLichDat(idLichDatSan);
        model.addAttribute("thanhToan", tt);
        return "Main/ThanhToan";
    }

    /**
     * API trả về trạng thái JSON (AJAX gọi để cập nhật trạng thái realtime)
     */
    @GetMapping("/thanh-toan/status/{id}")
    @ResponseBody
    public ThanhToan kiemTraTrangThai(@PathVariable("id") Integer idThanhToan) {
        return thanhToanService.kiemTraThanhToan(idThanhToan);
    }
    @GetMapping("/thanh-toan/thanh-cong")
    public String hienThiThanhCong() {
        return "Main/Success"; // chính là Success.html bạn có sẵn
    }
}
