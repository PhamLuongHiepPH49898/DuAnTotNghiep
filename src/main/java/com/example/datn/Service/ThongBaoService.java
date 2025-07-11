package com.example.datn.Service;

import com.example.datn.Entity.KhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.ThongBao;
import com.example.datn.Repository.ThongBaoRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThongBaoService {

    private final ThongBaoRepo thongBaoRepository;
    private final JavaMailSender javaMailSender;

    // Gửi thông báo xác nhận đặt sân
    public void taoThongBaoXacNhan(KhungGio khungGio, LichDatSan lichDatSan) {
        String noiDung = taoNoiDungThongBao(lichDatSan, khungGio, true);
        ThongBao thongBao = new ThongBao();
        thongBao.setTieuDe("Xác nhận đặt sân thành công");
        thongBao.setNoiDung(noiDung);
        thongBao.setNgayTao(LocalDateTime.now());
        thongBao.setTrangThai(0);
        thongBao.setTaiKhoan(lichDatSan.getTaiKhoan());
        thongBaoRepository.save(thongBao);

        sendEmail(lichDatSan.getTaiKhoan().getEmail(), thongBao.getTieuDe(), noiDung);
    }

    // Gửi thông báo nhắc lịch
    public void taoThongBaoNhacLich(LichDatSan lichDatSan, KhungGio khungGio) {
        String noiDung = taoNoiDungThongBao(lichDatSan, khungGio, false);
        ThongBao thongBao = new ThongBao();
        thongBao.setTieuDe("Nhắc lịch đá sân sắp tới");
        thongBao.setNoiDung(noiDung);
        thongBao.setNgayTao(LocalDateTime.now());
        thongBao.setTrangThai(0);
        thongBao.setTaiKhoan(lichDatSan.getTaiKhoan());
        thongBaoRepository.save(thongBao);

        sendEmail(lichDatSan.getTaiKhoan().getEmail(), thongBao.getTieuDe(), noiDung);
    }

    // Method tạo nội dung chung
    private String taoNoiDungThongBao(LichDatSan lichDatSan, KhungGio khungGio, boolean laXacNhan) {
        String tenSan = lichDatSan.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
        String checkInUrl = "http://localhost:8080/check-in/" + lichDatSan.getId();
        String greeting = "Xin chào " + lichDatSan.getTaiKhoan().getHo_ten() + ",\n\n";
        String content;

        if (laXacNhan) {
            content = greeting +
                    "Bạn đã ĐẶT SÂN thành công tại sân '" + tenSan + "'.\n\n" +
                    "⏰ Thời gian: " + lichDatSan.getNgayDat() + " lúc " + khungGio.getGioBatDau() + "\n" +
                    "🎯 Link Check-in: " + checkInUrl + "\n\n" +
                    "Cảm ơn bạn đã sử dụng dịch vụ!\n\n" +
                    "Trân trọng,\nĐội ngũ sân bóng.";
        } else {
            content = greeting +
                    "Đây là lời nhắc lịch đá sân sắp tới của bạn tại sân '" + tenSan + "'.\n\n" +
                    "⏰ Thời gian: " + lichDatSan.getNgayDat() + " lúc " + khungGio.getGioBatDau() + "\n" +
                    "🎯 Link Check-in: " + checkInUrl + "\n\n" +
                    "Chúc bạn có trận đấu vui vẻ!\n\n" +
                    "Trân trọng,\nĐội ngũ sân bóng.";
        }
        return content;
    }

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
        System.out.println("[DEBUG] Đang gửi mail đến: " + to);
        System.out.println("[DEBUG] Gửi mail thành công!");
    }
    // Gửi thông báo HUỶ đơn
    public void taoThongBaoHuy(LichDatSan lichDatSan, KhungGio khungGio) {
        String tenSan = lichDatSan.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
        String noiDung = "Xin chào " + lichDatSan.getTaiKhoan().getHo_ten() + ",\n\n"
                + "Đơn đặt sân tại '" + tenSan + "' vào ngày " + lichDatSan.getNgayDat()
                + " lúc " + khungGio.getGioBatDau() + " của bạn đã bị hủy bởi quản trị viên.\n\n"
                + "📝 Lý do hủy: " + (lichDatSan.getGhiChu() != null ? lichDatSan.getGhiChu() : "Không có") + "\n\n"
                + "Nếu có thắc mắc, vui lòng liên hệ với chúng tôi: sanbongsamba@gmail.com"
                + "Trân trọng,\nĐội ngũ sân bóng.";

        ThongBao thongBao = new ThongBao();
        thongBao.setTieuDe("Thông báo hủy đơn đặt sân");
        thongBao.setNoiDung(noiDung);
        thongBao.setNgayTao(LocalDateTime.now());
        thongBao.setTrangThai(0);
        thongBao.setTaiKhoan(lichDatSan.getTaiKhoan());
        thongBaoRepository.save(thongBao);

        sendEmail(lichDatSan.getTaiKhoan().getEmail(), thongBao.getTieuDe(), noiDung);
    }


    public List<ThongBao> layThongBaoTheoTaiKhoan(Integer idTaiKhoan) {
        return thongBaoRepository.findThongBaoUuTienChuaDoc(idTaiKhoan);
    }
    public int demThongBaoChuaDoc(Integer taiKhoanId) {
        return thongBaoRepository.countByTaiKhoanIdAndTrangThai(taiKhoanId, 0);
    }
    @Transactional
    public void danhDauThongBaoDaDoc(Integer idThongBao) {
        thongBaoRepository.danhDauDaDoc(idThongBao);
    }

    public Optional<ThongBao> layThongBaoTheoId(Integer idThongBao) {
        return thongBaoRepository.findById(idThongBao);
    }


}