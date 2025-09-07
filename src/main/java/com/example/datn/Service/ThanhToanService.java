package com.example.datn.Service;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.ThanhToan;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.ThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ThanhToanService {

    @Autowired
    private ThanhToanRepository thanhToanRepo;

    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    private final String API_KEY = "76NFBDEZWTSE0NB0GF2YLVJ4OZUSA5ICAH69ODUKMRTPQZGYKLHNQDCXQVI4PBNI";
    private final String API_URL = "https://my.sepay.vn/userapi/transactions/list";

    // ✅ Tạo giao dịch cho lịch đặt sân
    public ThanhToan taoGiaoDichChoLichDat(Integer idLichDatSan) {
        LichDatSan lich = lichDatSanRepo.findById(idLichDatSan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch đặt"));

        String ref = "SAMBA" + idLichDatSan;

        ThanhToan tt = new ThanhToan();
        tt.setSoTien(lich.getGiaApDung());
        tt.setReference(ref);
        tt.setTrangThai(0);
        tt.setLichDatSan(lich);

        if (lich.getTaiKhoan() != null) {
            tt.setTaiKhoan(lich.getTaiKhoan());
        } else {
            throw new RuntimeException("Lịch đặt chưa có tài khoản gắn với!");
        }

        tt.setHanThanhToan(LocalDateTime.now().plusMinutes(5));

        return thanhToanRepo.save(tt);
    }

    // ✅ Kiểm tra thanh toán bằng API SePay
    public ThanhToan kiemTraThanhToan(Integer idThanhToan) {
        ThanhToan tt = thanhToanRepo.findById(idThanhToan).orElseThrow();

        // Nếu đã quá hạn thì hủy
        if (tt.getHanThanhToan() != null && LocalDateTime.now().isAfter(tt.getHanThanhToan()) && tt.getTrangThai() == 0) {
            tt.setTrangThai(-1);
            thanhToanRepo.save(tt);

            // Hủy lịch đặt sân
            LichDatSan lich = tt.getLichDatSan();
            lich.setTrangThai(2);
            lich.setGhiChu("Quá hạn thanh toán");
            lichDatSanRepo.save(lich);

            return tt;
        }

        if (tt.getTrangThai() != 0) return tt;

        // 🚀 Gọi API SePay
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = API_URL + "?transaction_date_min=" + LocalDate.now();

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("transactions");

            if (data != null) {
                for (Map<String, Object> tx : data) {
                    String code = (String) tx.get("code");
                    String content = (String) tx.get("transaction_content");
                    String amountInStr = (String) tx.get("amount_in");

                    long amountIn = 0L;
                    try {
                        amountIn = new java.math.BigDecimal(amountInStr).longValue();
                    } catch (Exception ignored) {}

                    if ((code != null && code.equalsIgnoreCase(tt.getReference())
                            || (content != null && content.contains(tt.getReference())))
                            && amountIn == tt.getSoTien().longValue()) {
                        tt.setTrangThai(1); // ✅ đã thanh toán
                        thanhToanRepo.save(tt);

                        // Cập nhật lịch đặt
                        LichDatSan lich = tt.getLichDatSan();
                        lich.setTrangThai(1); // 1 = đã xác nhận
                        lichDatSanRepo.save(lich);

                        System.out.println("✅ Thanh toán thành công cho lichDatSan=" + lich.getId());
                        break;
                    }
                }
            }
        }

        return tt;
    }

    //Cron job: mỗi 30 giây
    @Scheduled(fixedDelay = 30000)
    public void autoCheckPayments() {
        List<ThanhToan> pending = thanhToanRepo.findByTrangThai(0);
        for (ThanhToan tt : pending) {
            try {
                kiemTraThanhToan(tt.getIdThanhToan());
            } catch (Exception e) {
                System.out.println("Lỗi khi kiểm tra id=" + tt.getIdThanhToan() + ": " + e.getMessage());
            }
        }
    }
}
