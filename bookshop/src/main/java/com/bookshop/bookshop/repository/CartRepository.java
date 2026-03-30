/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.model.GioHang;
import com.bookshop.bookshop.model.SanPham;
import com.bookshop.bookshop.model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 *
 * @author datp4
 */
public interface CartRepository extends JpaRepository<GioHang, Integer> {
    Optional<GioHang> findByTaiKhoanAndSanPham(TaiKhoan taiKhoan, SanPham sanPham);
}
