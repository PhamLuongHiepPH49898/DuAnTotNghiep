package com.example.datn.Service;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.LichDatSanRepo;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ThanhToanAutoService {

    private final LichDatSanRepo lichDatSanRepo;
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 60000) // mỗi 60 giây
    public void quetGiaoDichMB() {
        try {
            String apiUrl = "https://thueapi.pro/historyapimbbankv2/1c0f6f4998be5aa898928d0263ad08b0";
            JsonNode root = restTemplate.getForObject(apiUrl, JsonNode.class);

            if (root == null || !root.has("transactions")) {
                System.out.println("❌ Không có giao dịch nào.");
                return;
            }

            JsonNode data = root.get("transactions");
            if (!data.isArray()) {
                System.out.println("❌ 'transactions' không phải là mảng.");
                return;
            }

            System.out.println("🔁 Bắt đầu quét giao dịch...");

            for (JsonNode node : data) {
                String description = node.path("description").asText();
                String amountStr = node.path("creditAmount").asText("0").replace(",", "").trim();

                BigDecimal amount = new BigDecimal(amountStr.isEmpty() ? "0" : amountStr);
                System.out.println("📝 Nội dung: " + description);
                System.out.println("💰 Số tiền: " + amount);

                if (description.contains("SAMBA")) {
                    int idTaiKhoan = layIdTuNoiDung(description);
                    System.out.println("👤 Trích xuất ID tài khoản: " + idTaiKhoan);

                    if (idTaiKhoan == -1) continue;

                    LichDatSan lich = lichDatSanRepo
                            .findFirstByTaiKhoan_IdAndDaThanhToanFalseOrderByNgayTaoDesc(idTaiKhoan);

                    if (lich == null) {
                        System.out.println("⚠ Không tìm thấy lịch đặt sân chưa thanh toán cho tài khoản ID: " + idTaiKhoan);
                        continue;
                    }

                    System.out.println("✅ Lịch đặt sân tìm thấy: ID #" + lich.getId() + " - Giá áp dụng: " + lich.getGiaApDung());

                    if (lich.getGiaApDung().compareTo(amount) == 0) {
                        lich.setDaThanhToan(true);
                        lich.setTrangThai(1);
                        lichDatSanRepo.save(lich);
                        System.out.println("🎉 Đã DUYỆT sân tự động cho tài khoản ID: " + idTaiKhoan);
                    } else {
                        System.out.println("❌ Số tiền không khớp với lịch đặt sân.");
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi quét giao dịch MB:");
            e.printStackTrace();
        }
    }

    private int layIdTuNoiDung(String nd) {
        try {
            if (nd.contains("SAMBA")) {
                String[] parts = nd.split("SAMBA");
                String raw = parts[1].trim(); // "1- Ma GD ACSP/..."
                String idStr = raw.split("[^0-9]")[0]; // lấy "1"
                return Integer.parseInt(idStr);
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi trích xuất ID từ nội dung: " + nd);
            e.printStackTrace();
        }
        return -1;
    }
}
