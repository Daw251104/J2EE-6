package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.service.service.CartSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/giohang")
@RequiredArgsConstructor
public class GioHangController {

    private final CartSessionService cartSessionService;

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartSessionService.getCartItems());
        model.addAttribute("totalPrice", cartSessionService.getCartTotal());
        return "giohang/index";
    }
}
