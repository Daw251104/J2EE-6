/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.model.LoaiSanPham;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author datp4
 */
public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Integer> {
    Optional<LoaiSanPham> findByTenLoai(String tenLoai);
}