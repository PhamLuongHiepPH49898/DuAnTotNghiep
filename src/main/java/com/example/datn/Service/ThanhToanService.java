package com.example.datn.Service;

import com.example.datn.DTO.ThanhToanDTO;
import com.example.datn.Entity.KhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.ThanhToan;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.ThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ThanhToanService {

    @Autowired
    private ThanhToanRepository thanhToanRepo;

    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    @Autowired
    private ThongBaoService thongBaoService;

    private final String API_KEY = "76NFBDEZWTSE0NB0GF2YLVJ4OZUSA5ICAH69ODUKMRTPQZGYKLHNQDCXQVI4PBNI";
    private final String API_URL = "https://my.sepay.vn/userapi/transactions/list";

    /**
     * ✅ Tạo giao dịch cho nhiều lịch đặt sân (một tài khoản đặt nhiều sân)
     */
    public ThanhToan taoGiaoDichChoNhieuLichDat(List<Integer> idLichDatSan) {
        List<LichDatSan> lichList = lichDatSanRepo.findAllById(idLichDatSan);
        if (lichList.isEmpty()) {
            throw new RuntimeException("Không tìm thấy lịch đặt nào!");
        }

        // Tổng tiền
        double tongTien = lichList.stream().mapToDouble(LichDatSan::getGiaApDung).sum();

        // Tham chiếu (dùng timestamp để đảm bảo unique)
        String ref = "SAMBA" + System.currentTimeMillis();

        ThanhToan tt = new ThanhToan();
        tt.setSoTien(tongTien);
        tt.setReference(ref);
        tt.setTrangThai(0);
        tt.setTaiKhoan(lichList.get(0).getTaiKhoan()); // lấy tài khoản của lịch đầu tiên
        tt.setHanThanhToan(LocalDateTime.now().plusMinutes(5));

        // Gắn thanh toán vào từng lịch
        for (LichDatSan lich : lichList) {
            lich.setThanhToan(tt);
        }

        return thanhToanRepo.save(tt); // Cascade sẽ lưu luôn lichDatSans
    }

    /**
     * ✅ Kiểm tra thanh toán qua API SePay
     */
    public ThanhToanDTO kiemTraThanhToan(Integer idThanhToan) {
        ThanhToan tt = thanhToanRepo.findById(idThanhToan).orElseThrow();

        // Nếu đã quá hạn
        if (tt.getHanThanhToan() != null && LocalDateTime.now().isAfter(tt.getHanThanhToan()) && tt.getTrangThai() == 0) {
            tt.setTrangThai(-1);
            thanhToanRepo.save(tt);

            // Hủy tất cả lịch liên quan
            for (LichDatSan lich : tt.getLichDatSans()) {
                lich.setTrangThai(2); // đã hủy
                lich.setGhiChu("Quá hạn thanh toán");
                lichDatSanRepo.save(lich);

                // Kiểm tra xem đã có lịch với ngày và khung giờ này chưa
                boolean isExist = lichDatSanRepo.existsByNgayDatAndKhungGio(lich.getNgayDat(), lich.getGiaTheoKhungGio().getKhungGio());

                if (!isExist) {
                    // Tạo lịch mới chỉ khi chưa có lịch tồn tại
                    LichDatSan lichMoi = new LichDatSan();
                    lichMoi.setNgayDat(lich.getNgayDat());
                    lichMoi.setGiaTheoKhungGio(lich.getGiaTheoKhungGio());
                    lichMoi.setTrangThai(3); // 3 = trống
                    lichMoi.setGhiChu("Tạo lại sau khi huỷ quá hạn");
                    lichMoi.setNgayTao(LocalDateTime.now());
                    lichMoi.setGiaApDung(null);
                    lichMoi.setTaiKhoan(null);
                    lichDatSanRepo.save(lichMoi);

                    // Hủy -> gửi thông báo
                    try {
                        KhungGio khungGio = lich.getGiaTheoKhungGio().getKhungGio();
                        thongBaoService.taoThongBaoHuy(lich, khungGio);
                    } catch (Exception e) {
                        System.err.println("[WARN] Gửi thông báo HUỶ thất bại: " + e.getMessage());
                    }
                } else {
                    System.out.println("Lịch đã tồn tại, không tạo lịch mới.");
                }
            }
            return mapToDTO(tt);
        }

        if (tt.getTrangThai() != 0) return mapToDTO(tt); // Nếu đã thanh toán/hủy thì bỏ qua

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
                    } catch (Exception ignored) {
                    }

                    // Kiểm tra mã tham chiếu + số tiền
                    if ((code != null && code.equalsIgnoreCase(tt.getReference())
                            || (content != null && content.contains(tt.getReference())))
                            && amountIn == tt.getSoTien()) {

                        tt.setTrangThai(1); // ✅ đã thanh toán
                        thanhToanRepo.save(tt);

                        // Cập nhật tất cả lịch
                        for (LichDatSan lich : tt.getLichDatSans()) {
                            lich.setTrangThai(1); // 1 = đã xác nhận
                            lichDatSanRepo.save(lich);
                            //duyet -> gui thongbao
                            try {
                                KhungGio khungGio = lich.getGiaTheoKhungGio().getKhungGio();
                                thongBaoService.taoThongBaoXacNhan(khungGio, lich);
                            } catch (Exception e) {
                                System.err.println("[WARN] Gửi thông báo thất bại cho lịch ID=" + lich.getId() + ": " + e.getMessage());
                            }
                        }

                        System.out.println("✅ Thanh toán thành công cho ThanhToan ID=" + tt.getIdThanhToan());
                        break;
                    }
                }
            }
        }

        return mapToDTO(tt);
    }

    private ThanhToanDTO mapToDTO(ThanhToan tt) {
        return new ThanhToanDTO(
                tt.getIdThanhToan(),
                tt.getSoTien(),
                tt.getReference(),
                tt.getTrangThai()
        );
    }


    /**
     * ✅ Cron job tự động check mỗi 30s
     */
    @Scheduled(fixedDelay = 30000)
    public void autoCheckPayments() {
        List<ThanhToan> pending = thanhToanRepo.findByTrangThai(0);
        for (ThanhToan tt : pending) {
            try {
                kiemTraThanhToan(tt.getIdThanhToan());
            } catch (Exception e) {
                System.out.println("❌ Lỗi khi kiểm tra id=" + tt.getIdThanhToan() + ": " + e.getMessage());
            }
        }
    }
}
