package com.example.datn.Service;

import com.example.datn.Entity.*;
import com.example.datn.Repository.ThongBaoRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.math.BigDecimal;
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
    /*public void taoThongBaoNhacLich(LichDatSan lichDatSan, KhungGio khungGio) {
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
*/
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

    // Gửi thông báo HUỶ đơn từ phía người dùng
    public void taoThongBaoHuyNguoiDung(LichDatSan lichDatSan, KhungGio khungGio,HoanTien hoanTien) {
        String tenSan = lichDatSan.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
        BigDecimal phanTramHoanTien = BigDecimal.ZERO;
        BigDecimal soTienHoan = BigDecimal.ZERO;

        if (hoanTien != null) {
            phanTramHoanTien = hoanTien.getPhanTramHoan().multiply(BigDecimal.valueOf(100));
            soTienHoan = hoanTien.getSoTienHoan();
        }

        String noiDung = "<div style='font-family:sans-serif; color:#000;'>"
                         + "<p>Kính gửi: " + lichDatSan.getTaiKhoan().getHo_ten() + ",</p>"
                         + "<p>Bạn đã <strong>hủy đơn đặt sân</strong> tại sân <strong>\"" + tenSan + "\"</strong> "
                         + "ngày <strong>" + lichDatSan.getNgayDat() + "</strong> trong khung giờ "
                         + "<strong>" + khungGio.getGioBatDau() + " - " + khungGio.getGioKetThuc() + "</strong>.</p>"
                         // === Thêm đoạn này ===
                         + "<p style='color:#d9534f;'><em>Yêu cầu huỷ của bạn đã được gửi. "
                         + "Vui lòng chờ admin duyệt để hoàn tiền (nếu đủ điều kiện).</em></p>"
                         // ======================
                         + "<p><strong>Thông tin hoàn tiền (dự kiến):</strong></p>"
                         + "<ul>"
                         + "<li>Tỉ lệ hoàn tiền: <strong>" + phanTramHoanTien.stripTrailingZeros().toPlainString() + "%</strong></li>"
                         + "<li>Số tiền hoàn dự kiến: <strong>" + soTienHoan + " VNĐ</strong></li>"
                         + "</ul>"
                         + "<p>Khoản hoàn tiền sẽ được xử lý theo phương thức thanh toán bạn đã sử dụng sau khi được duyệt.</p>"
                         + "<p>Nếu có thắc mắc, vui lòng liên hệ qua email: "
                         + "<a href=\"mailto:sambasport.booking@gmail.com\">sambasport.booking@gmail.com</a></p>"
                         + "<p style='margin-top:16px;'>Trân trọng,<br><strong>Ban quản trị Sân bóng Samba</strong></p>"
                         + "</div>";

        ThongBao thongBao = new ThongBao();
        thongBao.setTieuDe(hoanTien != null ? "Xác nhận hủy đơn & hoàn tiền" : "Xác nhận hủy đơn");
        thongBao.setNoiDung(noiDung);
        thongBao.setNgayTao(LocalDateTime.now());
        thongBao.setTrangThai(0);
        thongBao.setTaiKhoan(lichDatSan.getTaiKhoan());
        thongBao.setLichDatSan(lichDatSan);

        thongBaoRepository.save(thongBao);
        sendEmail(lichDatSan.getTaiKhoan().getEmail(), thongBao.getTieuDe(), noiDung);
    }

    public void taoThongBaoDuyetHoanTien(LichDatSan lichDatSan,KhungGio khungGio, HoanTien hoanTien) {
        String tenSan = lichDatSan.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
        BigDecimal phanTramHoanTien = hoanTien.getPhanTramHoan().multiply(BigDecimal.valueOf(100));
        BigDecimal soTienHoan = hoanTien.getSoTienHoan();

        String noiDung = "<div style='font-family:sans-serif; color:#000;'>"
                + "<p>Kính gửi: " + lichDatSan.getTaiKhoan().getHo_ten() + ",</p>"
                + "<p>Yêu cầu <strong>hủy đơn đặt sân</strong> tại sân <strong>\"" + tenSan + "\"</strong> "
                + "ngày <strong>" + lichDatSan.getNgayDat() + "</strong> trong khung giờ "
                + "<strong>" + khungGio.getGioBatDau() + " - " + khungGio.getGioKetThuc() + "</strong> đã được duyệt và sẽ được hoàn theo thông tin dưới đây.</p>"
                + "<p><strong>Thông tin hoàn tiền:</strong></p>"
                + "<ul>"
                + "<li>Tỉ lệ hoàn tiền: <strong>" + phanTramHoanTien.stripTrailingZeros().toPlainString() + "%</strong></li>"
                + "<li>Số tiền hoàn dự kiến: <strong>" + soTienHoan + " VNĐ</strong></li>"
                + "</ul>"
                + "<p>Khoản hoàn tiền sẽ được xử lý theo phương thức thanh toán bạn đã sử dụng.</p>"
                + "<p>Nếu có thắc mắc, vui lòng liên hệ qua email: "
                + "<a href=\"mailto:sambasport.booking@gmail.com\">sambasport.booking@gmail.com</a></p>"
                + "<p style='margin-top:16px;'>Trân trọng,<br><strong>Ban quản trị Sân bóng Samba</strong></p>"
                + "</div>";

        ThongBao thongBao = new ThongBao();
        thongBao.setTieuDe("Xác nhận hoàn tiền");
        thongBao.setNoiDung(noiDung);
        thongBao.setNgayTao(LocalDateTime.now());
        thongBao.setTrangThai(0);
        thongBao.setTaiKhoan(lichDatSan.getTaiKhoan());
        thongBao.setLichDatSan(lichDatSan);

        thongBaoRepository.save(thongBao);
        sendEmail(lichDatSan.getTaiKhoan().getEmail(), thongBao.getTieuDe(), noiDung);
    }

    public void taoThongBaoTuChoiHoanTien(LichDatSan lichDatSan,
                                          KhungGio khungGio,
                                          HoanTien hoanTien) {
        String tenSan = lichDatSan.getGiaTheoKhungGio().getSanBong().getTen_san_bong();

        String noiDung = "<div style='font-family:sans-serif; color:#000;'>"
                + "<p>Kính gửi: " + lichDatSan.getTaiKhoan().getHo_ten() + ",</p>"
                + "<p>Yêu cầu <strong>hoàn tiền</strong> cho đơn đặt sân <strong>\"" + tenSan + "\"</strong> "
                + "ngày <strong>" + lichDatSan.getNgayDat() + "</strong> "
                + "khung giờ <strong>" + khungGio.getGioBatDau() + " - " + khungGio.getGioKetThuc() + "</strong> "
                + "đã <strong>KHÔNG ĐƯỢC DUYỆT</strong>.</p>"
                + "<p>Nếu cần hỗ trợ, vui lòng liên hệ: "
                + "<a href=\"mailto:sambasport.booking@gmail.com\">sambasport.booking@gmail.com</a></p>"
                + "<p style='margin-top:16px;'>Trân trọng,<br><strong>Ban quản trị Sân bóng Samba</strong></p>"
                + "</div>";

        ThongBao thongBao = new ThongBao();
        thongBao.setTieuDe("Thông báo từ chối hoàn tiền");
        thongBao.setNoiDung(noiDung);
        thongBao.setNgayTao(LocalDateTime.now());
        thongBao.setTrangThai(0);
        thongBao.setTaiKhoan(lichDatSan.getTaiKhoan());
        thongBao.setLichDatSan(lichDatSan);

        thongBaoRepository.save(thongBao);
        sendEmail(lichDatSan.getTaiKhoan().getEmail(), thongBao.getTieuDe(), noiDung);
    }
    public void taoThongBaoHuyNguoiDungChoAdmin(
            LichDatSan lichDatSan,
            KhungGio khungGio,
            String ghiChu,
            TaiKhoan admin) {

        String tenSan = lichDatSan.getGiaTheoKhungGio()
                .getSanBong().getTen_san_bong();

        String noiDung = "<p>Người dùng <strong>"
                + lichDatSan.getTaiKhoan().getHo_ten()
                + "</strong> đã hủy đặt sân <strong>"
                + tenSan + "</strong> ngày <strong>"
                + lichDatSan.getNgayDat() + "</strong> "
                + "khung giờ <strong>" + khungGio.getGioBatDau()
                + " - " + khungGio.getGioKetThuc() + "</strong>."
                + "<br/>Ghi chú: " + (ghiChu != null ? ghiChu : "Không có")
                + "</p>";

        ThongBao tb = new ThongBao();
        tb.setTieuDe("Người dùng đã hủy lịch đặt sân");
        tb.setNoiDung(noiDung);
        tb.setNgayTao(LocalDateTime.now());
        tb.setTrangThai(0);
        tb.setTaiKhoan(admin);         // Gửi cho Admin
        tb.setLichDatSan(lichDatSan);

        thongBaoRepository.save(tb);
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