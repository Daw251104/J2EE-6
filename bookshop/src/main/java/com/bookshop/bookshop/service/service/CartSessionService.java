package com.bookshop.bookshop.service.service;

import com.bookshop.bookshop.dto.CartItem;
import com.bookshop.bookshop.model.SanPham;
import com.bookshop.bookshop.repository.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope
@RequiredArgsConstructor
public class CartSessionService {

    private final SanPhamRepository sanPhamRepository;
    
    // Lưu các sản phẩm trong Session
    private final List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(Integer sanPhamId, Integer soLuong) {
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getMaSP().equals(sanPhamId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setSoLuong(item.getSoLuong() + soLuong);
        } else {
            SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));
            CartItem newItem = new CartItem(
                sanPham.getMaSP(), 
                sanPham.getTenSP(), 
                sanPham.getHinhDaiDien(), 
                sanPham.getGiaBan(), 
                soLuong
            );
            cartItems.add(newItem);
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public BigDecimal getCartTotal() {
        return cartItems.stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public void clearCart() {
        cartItems.clear();
    }
}
