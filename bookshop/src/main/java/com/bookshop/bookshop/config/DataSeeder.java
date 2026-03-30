/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookshop.bookshop.config;

import com.bookshop.bookshop.model.*;
import com.bookshop.bookshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Bean;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {


    private final UserRepository taiKhoanRepository;
    private final RoleRepository loaiTaiKhoanRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            System.out.println("===== DataSeeder BẮT ĐẦU CHẠY =====");

            // 1. Tạo các loại tài khoản (roles) nếu chưa tồn tại
            LoaiTaiKhoan adminRole = loaiTaiKhoanRepository.findByTenLoaiTK("ADMIN")
                    .orElseGet(() -> {
                        LoaiTaiKhoan role = new LoaiTaiKhoan();
                        role.setTenLoaiTK("ADMIN");
                        return loaiTaiKhoanRepository.save(role);
                    });

            LoaiTaiKhoan staffRole = loaiTaiKhoanRepository.findByTenLoaiTK("STAFF")
                    .orElseGet(() -> {
                        LoaiTaiKhoan role = new LoaiTaiKhoan();
                        role.setTenLoaiTK("STAFF");
                        return loaiTaiKhoanRepository.save(role);
                    });

            LoaiTaiKhoan customerRole = loaiTaiKhoanRepository.findByTenLoaiTK("CUSTOMER")
                    .orElseGet(() -> {
                        LoaiTaiKhoan role = new LoaiTaiKhoan();
                        role.setTenLoaiTK("CUSTOMER");
                        return loaiTaiKhoanRepository.save(role);
                    });

            // 2. Tạo tài khoản mẫu nếu chưa có
            if (taiKhoanRepository.findByUsername("admin").isEmpty()) {
                TaiKhoan admin = TaiKhoan.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))  // thay đổi mật khẩu thật khi deploy
                        .hoTen("Quản trị viên")
                        .gioiTinh(GioiTinh.NAM)
                        .sdt("0909123456")
                        .email("admin@petshop.vn")
                        .diaChi("Quận 1, TP.HCM")
                        .trangThai(TrangThaiTaiKhoan.ACTIVE)
                        .anhDaiDien(null)  // hoặc "/uploads/avatars/admin_default.jpg" nếu có ảnh mặc định
                        .loaiTaiKhoan(Set.of(adminRole))
                        .build();

                taiKhoanRepository.save(admin);
                System.out.println("Đã tạo tài khoản ADMIN mặc định");
            }

            if (taiKhoanRepository.findByUsername("staff").isEmpty()) {
                TaiKhoan staff = TaiKhoan.builder()
                        .username("staff")
                        .password(passwordEncoder.encode("staff123"))
                        .hoTen("Nhân viên bán hàng")
                        .gioiTinh(GioiTinh.NU)
                        .sdt("0918234567")
                        .email("staff@petshop.vn")
                        .diaChi("Quận 3, TP.HCM")
                        .trangThai(TrangThaiTaiKhoan.ACTIVE)
                        .anhDaiDien(null)
                        .loaiTaiKhoan(Set.of(staffRole))
                        .build();

                taiKhoanRepository.save(staff);
                System.out.println("Đã tạo tài khoản STAFF mẫu");
            }

            if (taiKhoanRepository.findByUsername("customer").isEmpty()) {
                TaiKhoan customer = TaiKhoan.builder()
                        .username("customer")
                        .password(passwordEncoder.encode("123456"))
                        .hoTen("Khách hàng thân thiết")
                        .gioiTinh(GioiTinh.NAM)
                        .sdt("0987654321")
                        .email("khach@petshop.vn")
                        .diaChi("Bình Thạnh, TP.HCM")
                        .trangThai(TrangThaiTaiKhoan.ACTIVE)
                        .anhDaiDien(null)
                        .loaiTaiKhoan(Set.of(customerRole))
                        .build();

                taiKhoanRepository.save(customer);
                System.out.println("Đã tạo tài khoản CUSTOMER mẫu");
            }

            System.out.println("===== DataSeeder KẾT THÚC =====");
        };
}
}