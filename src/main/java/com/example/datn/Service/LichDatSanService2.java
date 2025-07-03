package com.example.datn.Service;

import com.example.datn.DTO.LichDatSanForm;
import com.example.datn.Entity.LichDatSan;

import java.util.List;

public interface LichDatSanService2 {
    /** Lấy lịch đặt theo id nhưng chỉ khi thuộc về user; ném lỗi nếu không */
    LichDatSan findByIdForUser(Integer id, Integer userId);

    /** Sửa lịch đặt – chỉ khi còn ≥ cutoff giờ  */
    LichDatSan updateBooking(Integer id, LichDatSanForm form, Integer userId);

    /** Hủy lịch đặt – chỉ khi còn ≥ cutoff giờ */
    void cancelBooking(Integer id, Integer userId);

    /** Lấy toàn bộ lịch của user (để hiển thị “Lịch đặt của tôi”) */
    List<LichDatSan> getBookingsOfUser(Integer userId);
}
