package com.bookshop.bookshop.service.service;

import com.bookshop.bookshop.model.GioHang;
import com.bookshop.bookshop.model.SanPham;
import com.bookshop.bookshop.model.TaiKhoan;
import com.bookshop.bookshop.repository.CartRepository;
import com.bookshop.bookshop.repository.SanPhamRepository;
import com.bookshop.bookshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final SanPhamRepository sanPhamRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addToCart(Integer sanPhamId, Integer soLuong) {
        String username = getLoggedInUsername();
        if (username.equals("anonymousUser")) {
            throw new RuntimeException("Vui lòng đăng nhập để thêm vào giỏ hàng!");
        }

        TaiKhoan taiKhoan = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));

        Optional<GioHang> existingItem = cartRepository.findByTaiKhoanAndSanPham(taiKhoan, sanPham);

        if (existingItem.isPresent()) {
            GioHang item = existingItem.get();
            item.setSoLuong(item.getSoLuong() + soLuong);
            cartRepository.save(item);
        } else {
            GioHang newItem = new GioHang();
            newItem.setTaiKhoan(taiKhoan);
            newItem.setSanPham(sanPham);
            newItem.setSoLuong(soLuong);
            cartRepository.save(newItem);
        }
    }

    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
