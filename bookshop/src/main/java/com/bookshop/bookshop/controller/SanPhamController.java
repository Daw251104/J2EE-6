package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.SanPham;
import com.bookshop.bookshop.repository.LoaiSanPhamRepository;
import com.bookshop.bookshop.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookshop.bookshop.dto.SanPhamDTO;
import com.bookshop.bookshop.service.service.SanPhamService;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.bookshop.bookshop.dto.SanPhamResponseDTO;

@Controller
@RequestMapping("/sanpham")
public class SanPhamController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    private SanPhamService sanPhamService;


    // Xem danh sách sản phẩm (Mọi người đều xem được)
    @GetMapping
    public String listSanPham(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "maLoai", required = false) Integer maLoai,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "asc") String sort,
            Model model) {

        int pageSize = 5;
        Sort sortOrder = sort.equalsIgnoreCase("desc") ?
                Sort.by("giaBan").descending() : Sort.by("giaBan").ascending();
        Pageable pageable = PageRequest.of(page, pageSize, sortOrder);

        Page<SanPhamResponseDTO> sanPhamPage = sanPhamService.getSanPhamsPaged(keyword, maLoai, pageable);

        model.addAttribute("sanPhamPage", sanPhamPage);
        model.addAttribute("loaiSanPhams", loaiSanPhamRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("maLoai", maLoai);
        model.addAttribute("sort", sort);

        return "sanpham/index";
    }

    // Hiển thị form Thêm sản phẩm (Chỉ OWNER/CHU_CUA_HANG)
    @GetMapping("/them")
    public String showAddForm(Model model) {
        model.addAttribute("sanPham", new SanPhamDTO());
        model.addAttribute("loaiSanPhams", loaiSanPhamRepository.findAll());
        return "sanpham/form";
    }

    // Xử lý Thêm sản phẩm (Chỉ OWNER/CHU_CUA_HANG)
    @PostMapping("/them")
    public String addSanPham(@ModelAttribute("sanPham") SanPhamDTO sanPhamDTO, RedirectAttributes redirectAttributes) {
        try {
            sanPhamService.createSanPham(sanPhamDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
        return "redirect:/sanpham";
    }

    // Hiển thị form Sửa sản phẩm (Chỉ OWNER/CHU_CUA_HANG)
    @GetMapping("/sua/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        SanPham sanPham = sanPhamRepository.findById(id).orElse(null);
        if (sanPham == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm!");
            return "redirect:/sanpham";
        }

        SanPhamDTO dto = new SanPhamDTO();
        dto.setMaSP(sanPham.getMaSP());
        dto.setTenSP(sanPham.getTenSP());
        dto.setGiaBan(sanPham.getGiaBan());
        dto.setSlTon(sanPham.getSlTon());
        dto.setKhuyenMai(sanPham.getKhuyenMai());
        dto.setTinhTrang(sanPham.getTinhTrang());
        dto.setMoTa(sanPham.getMoTa());
        if (sanPham.getLoaiSanPham() != null) {
            dto.setMaLoai(sanPham.getLoaiSanPham().getMaLoai());
        }

        model.addAttribute("sanPham", dto);
        model.addAttribute("oldHinhDaiDien", sanPham.getHinhDaiDien());

        model.addAttribute("loaiSanPhams", loaiSanPhamRepository.findAll());
        return "sanpham/form";
    }

    // Xử lý Cập nhật sản phẩm (Chỉ OWNER/CHU_CUA_HANG)
    @PostMapping("/sua/{id}")
    public String editSanPham(@PathVariable("id") Integer id, @ModelAttribute("sanPham") SanPhamDTO sanPhamDTO,
            RedirectAttributes redirectAttributes) {
        try {
            sanPhamService.updateSanPham(id, sanPhamDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Lỗi: Không tìm thấy sản phẩm hoặc thông tin không hợp lệ! " + e.getMessage());
        }
        return "redirect:/sanpham";
    }

    // Xử lý Xóa sản phẩm (Chỉ OWNER/CHU_CUA_HANG)
    @GetMapping("/xoa/{id}")
    public String deleteSanPham(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            sanPhamRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa sản phẩm vì có dữ liệu liên quan!");
        }
        return "redirect:/sanpham";
    }
}
