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
        String diaChi = lichDatSan.getGiaTheoKhungGio().getSanBong().getDia_chi();

        String noiDung = "<div style='font-family:sans-serif; color:#000;'>"
                + "<p>Xin chào " + lichDatSan.getTaiKhoan().getHo_ten() + ",</p>"
                + "<p>Chúng tôi xác nhận bạn đã <strong>đặt sân thành công</strong> tại sân <strong>" + tenSan + "</strong>.</p>"
                + "<p><strong> Mã đơn:</strong> " + lichDatSan.getId() + "</p>"
                + "<p><strong> Địa chỉ sân:</strong> " + diaChi + "</p>"
                + "<p><strong> Thời gian:</strong> " + lichDatSan.getNgayDat()
                + " từ <strong>" + khungGio.getGioBatDau() + "</strong> đến <strong>" + khungGio.getGioKetThuc() + "</strong></p>"
                + "<p>Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi.</p>"
                + "<p style='margin-top:16px;'>Trân trọng,<br><strong>Đội ngũ sân bóng</strong></p>"
                + "</div>";

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
        String diaChi = lichDatSan.getGiaTheoKhungGio().getSanBong().getDia_chi();

        String checkInUrl = "http://localhost:8080/check-in/" + lichDatSan.getId();

        String noiDung = "<div style='font-family:sans-serif; color:#000;'>"
                + "<p>Xin chào " + lichDatSan.getTaiKhoan().getHo_ten() + ",</p>"
                + "<p>Đây là lời nhắc về lịch đặt sân sắp tới của bạn tại sân <strong>" + tenSan + "</strong>.</p>"
                + "<p><strong> Thời gian:</strong> " + lichDatSan.getNgayDat()
                + " từ <strong>" + khungGio.getGioBatDau() + "</strong> đến <strong>" + khungGio.getGioKetThuc() + "</strong></p>"
                + "<p> <strong>Link Check-in:</strong> <a href=\"" + checkInUrl + "\" style='color:#1a73e8; text-decoration:none;'>Bấm vào đây để check-in</a></p>"

                + "<p>Chúc bạn có một trận đấu thật vui vẻ và hiệu quả!</p>"
                + "<p style='margin-top:16px;'>Trân trọng,<br><strong>Đội ngũ sân bóng</strong></p>"
                + "</div>";



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
    public void taoThongBaoNhacLichTruoc1h(LichDatSan lichDatSan, KhungGio khungGio) {
        String tenSan = lichDatSan.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
        String diaChi = lichDatSan.getGiaTheoKhungGio().getSanBong().getDia_chi();

        String checkInUrl = "http://localhost:8080/check-in/" + lichDatSan.getId();

        String noiDung = "<div style='font-family:sans-serif; color:#000;'>"
                + "<p>Xin chào " + lichDatSan.getTaiKhoan().getHo_ten() + ",</p>"
                + "<p>Đây là lời nhắc về lịch đặt sân sắp tới của bạn tại sân <strong>" + tenSan + "</strong>.</p>"
                + "<p><strong> Địa chỉ sân:</strong> " + diaChi + "</p>"
                + "<p><strong> Thời gian:</strong> " + lichDatSan.getNgayDat()
                + " từ <strong>" + khungGio.getGioBatDau() + "</strong> đến <strong>" + khungGio.getGioKetThuc() + "</strong></p>"
                + "<p><strong> Link Check-in:</strong> "
                + "<a href=\"" + checkInUrl + "\" style='color:#1a73e8; text-decoration:none;'>Bấm vào đây để check-in</a></p>"
                + "<p>Chúc bạn có một trận đấu thật vui vẻ và hiệu quả!</p>"
                + "<p style='margin-top:16px;'>Trân trọng,<br><strong>Đội ngũ sân bóng</strong></p>"
                + "</div>";




        ThongBao thongBao = new ThongBao();
        thongBao.setTieuDe("Nhắc lịch đá sân");
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
        String diaChi = lichDatSan.getGiaTheoKhungGio().getSanBong().getDia_chi();
        String noiDung = "<div style='font-family:sans-serif; color:#000;'>"
                + "<p>Kính gửi: " + lichDatSan.getTaiKhoan().getHo_ten() + ",</p>"
                + "<p>Chúng tôi xin thông báo rằng đơn đặt sân tại <strong>\"" + tenSan + "</strong>.</p>"
                + "<p><strong> Địa chỉ sân:</strong> " + diaChi + "</p>"
                + "<p><strong> Thời gian:</strong> " + lichDatSan.getNgayDat()
                + " từ <strong>" + khungGio.getGioBatDau() + "</strong> đến <strong>" + khungGio.getGioKetThuc() + "</strong></p>"                + "<p><strong>Lý do hủy:</strong> " + (lichDatSan.getGhiChu() != null ? lichDatSan.getGhiChu() : "Không có") + "</p>"
                + "<p>Chúng tôi rất tiếc vì sự bất tiện này. Nếu bạn cần hỗ trợ, vui lòng liên hệ qua email: "
                + "<a href=\"mailto:sambasport.booking@gmail.com\">sambasport.booking@gmail.com</a></p>"
                + "<p style='margin-top:16px;'>Trân trọng,<br><strong>Ban quản trị Sân bóng Samba</strong></p>"
                + "</div>";

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