package com.bookshop.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Integer maSP;
    private String tenSP;
    private String hinhDaiDien;
    private BigDecimal giaBan;
    private Integer soLuong;

    public BigDecimal getTotal() {
        return giaBan.multiply(new BigDecimal(soLuong));
    }
}
