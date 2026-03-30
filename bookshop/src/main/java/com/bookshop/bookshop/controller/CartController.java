package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.service.service.CartSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartSessionService cartSessionService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Integer> payload) {
        try {
            Integer sanPhamId = payload.get("sanPhamId");
            Integer soLuong = payload.getOrDefault("soLuong", 1);
            
            if (sanPhamId == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Mã sản phẩm không hợp lệ"));
            }

            cartSessionService.addToCart(sanPhamId, soLuong);
            return ResponseEntity.ok(Map.of("message", "Thêm vào giỏ hàng thành công!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi: " + e.getMessage()));
        }
    }
}
