//
//package com.example.datn.Controller;
//
//import com.example.datn.DTO.ThongKeDoanhThuDTO;
//import com.example.datn.Repository.ThanhToanRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//public class ThongKeController {
//
//    @Autowired
//    private ThanhToanRepo thanhToanRepo;
//
//    @GetMapping("/thongke/doanhthu")
//    public String hienThiBieuDo(
//            @RequestParam(required = false) Integer thang,
//            @RequestParam(required = false) Integer nam,
//            Model model
//    ) {
//        // Nếu không chọn thì mặc định là tháng hiện tại
//        LocalDate today = LocalDate.now();
//        int thangHienTai = thang != null ? thang : today.getMonthValue();
//        int namHienTai = nam != null ? nam : today.getYear();
//
//        List<Object[]> rawData = thanhToanRepo.thongKeTheoSanVaThangVaNam(thangHienTai, namHienTai);
//
//        // Xử lý dữ liệu thành Map<San, Map<Thang, DoanhThu>> giống cũ
//        Map<String, Map<Integer, BigDecimal>> chartData = new LinkedHashMap<>();
//        for (Object[] row : rawData) {
//            String tenSan = (String) row[0];
//            Integer thangDb = (Integer) row[1];
//            BigDecimal doanhThu = (BigDecimal) row[2];
//            chartData.computeIfAbsent(tenSan, k -> new HashMap<>()).put(thangDb, doanhThu);
//        }
//
//        model.addAttribute("chartData", chartData);
//        model.addAttribute("thangChon", thangHienTai);
//        model.addAttribute("namChon", namHienTai);
//
//        return "ThongKe/DoanhThuTheoSan";
//    }
//
//
//}
//
