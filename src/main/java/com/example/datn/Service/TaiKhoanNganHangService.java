package com.example.datn.Service;

import com.example.datn.Entity.TaiKhoanNganHang;
import com.example.datn.Repository.TaiKhoanNganHangRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TaiKhoanNganHangService {

    @Autowired private TaiKhoanNganHangRepository repo;

    public TaiKhoanNganHang getByIdOrDefault(Integer accId) {
        if (accId != null) {
            return repo.findById(accId)
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy tài khoản id=" + accId));
        }
        return repo.findFirstByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("Chưa cấu hình tài khoản ngân hàng mặc định (active=true)"));
    }

    @Transactional
    public void capNhat(Integer id, String bankCode, String tenNganHang,
                        String soTaiKhoan, String chuTaiKhoan, boolean macDinh) {

        var tk = repo.findById(id).orElseThrow();
        tk.setBankCode(bankCode);
        tk.setTenNganHang(tenNganHang);
        tk.setSoTaiKhoan(soTaiKhoan);
        tk.setChuTaiKhoan(chuTaiKhoan);

        if (macDinh) {
            repo.unsetDefaultExcept(id); // tắt các bản ghi khác
            tk.setActive(true);
        }
        repo.save(tk);
    }

    @Transactional
    public void taoMoi(String bankCode, String tenNganHang,
                       String soTaiKhoan, String chuTaiKhoan,
                       boolean macDinh) {

        if (macDinh) {
            // Tắt tất cả mặc định hiện có
            repo.unsetAllDefault();
        }

        TaiKhoanNganHang tk = new TaiKhoanNganHang();
        tk.setBankCode(bankCode);
        tk.setTenNganHang(tenNganHang);
        tk.setSoTaiKhoan(soTaiKhoan);
        tk.setChuTaiKhoan(chuTaiKhoan);
        tk.setActive(macDinh);        // chỉ true khi tick “mặc định”
        repo.save(tk);
    }
}

