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
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        String tenSan = lichDatSan.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
        String noiDung = "Xin chào " + lichDatSan.getTaiKhoan().getHo_ten() + ",\n\n"
                + "Bạn đã ĐẶT SÂN thành công tại sân '" + tenSan + "'.\n\n"
                + "⏰ Thời gian: " + lichDatSan.getNgayDat()
                + " lúc " + khungGio.getGioBatDau() + " - " + khungGio.getGioKetThuc() + "\n"
                + "Cảm ơn bạn đã sử dụng dịch vụ!\n\n"
                + "Trân trọng,\nĐội ngũ sân bóng.";

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
        String tenSan = lichDatSan.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
        String checkInUrl = "http://localhost:8080/check-in/" + lichDatSan.getId();

        String noiDung = "Xin chào " + lichDatSan.getTaiKhoan().getHo_ten() + ",\n\n"
                + "Đây là lời nhắc lịch đá sân sắp tới của bạn tại sân '" + tenSan + "'.\n\n"
                + "⏰ Thời gian: " + lichDatSan.getNgayDat()
                + " lúc " + khungGio.getGioBatDau() + " - " + khungGio.getGioKetThuc() + "\n"
                + "🎯 Link Check-in: <a href=\"" + checkInUrl + "\">Bấm vào đây để check-in</a>\n\n"
                + "Chúc bạn có trận đấu vui vẻ!\n\n"
                + "Trân trọng,\nĐội ngũ sân bóng.";

        ThongBao thongBao = new ThongBao();
        thongBao.setTieuDe("Bạn có lịch đá sân vào ngày mai");
        thongBao.setNoiDung(noiDung);
        thongBao.setNgayTao(LocalDateTime.now());
        thongBao.setTrangThai(0);
        thongBao.setTaiKhoan(lichDatSan.getTaiKhoan());
        thongBao.setLichDatSan(lichDatSan);

        thongBaoRepository.save(thongBao);
        sendEmail(lichDatSan.getTaiKhoan().getEmail(), thongBao.getTieuDe(), noiDung);
    }

    //
    public void taoThongBaoTruoc2H(LichDatSan lichDatSan, KhungGio khungGio) {
        String tenSan = lichDatSan.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
        String checkInUrl = "http://localhost:8080/check-in/" + lichDatSan.getId();

        String noiDung = "Xin chào " + lichDatSan.getTaiKhoan().getHo_ten() + ",\n\n"
                + "Bạn sắp đến giờ đá sân tại sân '" + tenSan + "'.\n\n"
                + "⏰ Thời gian: " + lichDatSan.getNgayDat() + " lúc " + khungGio.getGioBatDau()
                + " - " + khungGio.getGioKetThuc() + "\n"
                + "🎯 Link Check-in: " + checkInUrl + "\n\n"
                + "Chúc bạn có một trận đấu vui vẻ!\n\n"
                + "Trân trọng,\nĐội ngũ sân bóng.";

        ThongBao thongBao = new ThongBao();
        thongBao.setTieuDe("Nhắc lịch đá sân - còn 2 giờ");
        thongBao.setNoiDung(noiDung);
        thongBao.setNgayTao(LocalDateTime.now());
        thongBao.setTrangThai(0);
        thongBao.setTaiKhoan(lichDatSan.getTaiKhoan());
        thongBao.setLichDatSan(lichDatSan); // để kiểm tra đã gửi chưa

        thongBaoRepository.save(thongBao);

        sendEmail(lichDatSan.getTaiKhoan().getEmail(), thongBao.getTieuDe(), noiDung);
    }
    // Gửi thông báo HUỶ đơn
    public void taoThongBaoHuy(LichDatSan lichDatSan, KhungGio khungGio) {
        String tenSan = lichDatSan.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
        String noiDung = "Xin chào " + lichDatSan.getTaiKhoan().getHo_ten() + ",\n\n"
                + "Đơn đặt sân tại '" + tenSan + "' vào ngày " + lichDatSan.getNgayDat()
                + " lúc " + khungGio.getGioBatDau() + " - " + khungGio.getGioKetThuc() + " của bạn đã bị hủy bởi quản trị viên.\n\n"
                + " Lý do hủy: " + (lichDatSan.getGhiChu() != null ? lichDatSan.getGhiChu() : "Không có") + "\n\n"
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

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // 'true' nghĩa là gửi HTML

            javaMailSender.send(message);
            System.out.println("[DEBUG] Gửi email HTML thành công đến: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("[ERROR] Gửi email thất bại đến: " + to);
        }
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