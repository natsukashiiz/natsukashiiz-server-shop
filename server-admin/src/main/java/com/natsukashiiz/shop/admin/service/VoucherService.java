package com.natsukashiiz.shop.admin.service;

import com.natsukashiiz.shop.admin.model.dto.VoucherDTO;
import com.natsukashiiz.shop.common.DiscountType;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.common.VoucherStatus;
import com.natsukashiiz.shop.entity.Admin;
import com.natsukashiiz.shop.entity.Voucher;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.VoucherException;
import com.natsukashiiz.shop.model.resposne.PageResponse;
import com.natsukashiiz.shop.repository.CategoryRepository;
import com.natsukashiiz.shop.repository.ProductRepository;
import com.natsukashiiz.shop.repository.VoucherRepository;
import com.natsukashiiz.shop.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final AuthService authService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public PageResponse<List<VoucherDTO>> queryAllCategories(VoucherDTO request, PaginationRequest pagination) {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("id", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("code", ExampleMatcher.GenericPropertyMatcher::contains)
                .withMatcher("discountType", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("status", ExampleMatcher.GenericPropertyMatcher::exact);

        Page<Voucher> page = voucherRepository.findAll(Example.of(request.toEntity(), matcher), pagination);
        List<VoucherDTO> vouchers = page.getContent()
                .stream()
                .map(VoucherDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageResponse<>(vouchers, page.getTotalElements());
    }

    public VoucherDTO queryVoucherById(Long voucherId) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        if (!voucherOptional.isPresent()) {
            log.warn("QueryVoucherById-[block]:(voucher not found). voucherId:{}, adminId:{}", voucherId, admin.getId());
            throw VoucherException.invalid();
        }

        return VoucherDTO.fromEntity(voucherOptional.get());
    }

    public VoucherDTO createVoucher(VoucherDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        if (ValidationUtils.invalidVoucherCode(request.getCode())) {
            log.warn("CreateVoucher-[block]:(code is invalid). adminId:{}", admin.getId());
            throw VoucherException.invalidCode();
        }

        if (ValidationUtils.invalidLessThanZero(request.getDiscount())) {
            log.warn("CreateVoucher-[block]:(discount is invalid). adminId:{}", admin.getId());
            throw VoucherException.invalidDiscount();
        }

        if (ObjectUtils.isEmpty(request.getDiscountType())) {
            log.warn("CreateVoucher-[block]:(discountType is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidDiscountType();
        }

        if (DiscountType.PERCENT.equals(request.getDiscountType())) {
            if (ObjectUtils.isEmpty(request.getMaxDiscount())) {
                log.warn("CreateVoucher-[block]:(maxDiscount is empty). adminId:{}", admin.getId());
                throw VoucherException.invalidMaxDiscount();
            }

            if (ValidationUtils.invalidMaxValue(request.getMaxDiscount(), request.getDiscount())) {
                log.warn("CreateVoucher-[block]:(maxDiscount is greater than discount). adminId:{}", admin.getId());
                throw VoucherException.invalidMaxDiscount();
            }

            if (request.getMaxDiscount() < 0 || request.getMaxDiscount() > 100) {
                log.warn("CreateVoucher-[block]:(maxDiscount is invalid). adminId:{}", admin.getId());
                throw VoucherException.invalidMaxDiscount();
            }
        }

        if (ValidationUtils.invalidLessThanZero(request.getMinOrderPrice())) {
            log.warn("CreateVoucher-[block]:(minOrderPrice is invalid). adminId:{}", admin.getId());
            throw VoucherException.invalidMinOrderPrice();
        }

        if (ValidationUtils.invalidLessThanZero(request.getQuantity())) {
            log.warn("CreateVoucher-[block]:(quantity is invalid). adminId:{}", admin.getId());
            throw VoucherException.invalidQuantity();
        }

        if (request.getQuantity() < 0) {
            log.warn("CreateVoucher-[block]:(quantity is invalid). adminId:{}", admin.getId());
            throw VoucherException.invalidQuantity();
        }

        if (ValidationUtils.invalidBeginAt(request.getBeginAt())) {
            log.warn("CreateVoucher-[block]:(beginAt is invalid). adminId:{}", admin.getId());
            throw VoucherException.invalidBeginAt();
        }

        if (ValidationUtils.invalidExpiredAt(request.getBeginAt(), request.getExpiredAt())) {
            log.warn("CreateVoucher-[block]:(expiredAt is invalid). adminId:{}", admin.getId());
            throw VoucherException.invalidExpiredAt();
        }

        if (ObjectUtils.isEmpty(request.getStatus())) {
            log.warn("CreateVoucher-[block]:(status is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidStatus();
        }

        if (ObjectUtils.isEmpty(request.getThumbnail())) {
            log.warn("CreateVoucher-[block]:(thumbnail is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidThumbnail();
        }

        if (!ObjectUtils.isEmpty(request.getProduct())) {
            boolean exists = productRepository.existsById(request.getProduct().getId());
            if (!exists) {
                log.warn("CreateVoucher-[block]:(product not found). productId:{}, adminId:{}", request.getProduct().getId(), admin.getId());
                throw VoucherException.invalidProduct();
            }
        }

        if (!ObjectUtils.isEmpty(request.getCategory())) {
            boolean exists = categoryRepository.existsById(request.getCategory().getId());
            if (!exists) {
                log.warn("CreateVoucher-[block]:(category not found). categoryId:{}, adminId:{}", request.getCategory().getId(), admin.getId());
                throw VoucherException.invalidCategory();
            }
        }

        Voucher voucher = request.toEntity();
        Voucher saved = voucherRepository.save(voucher);
        return VoucherDTO.fromEntity(saved);
    }

    public VoucherDTO updateVoucherById(Long voucherId, VoucherDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        if (!voucherOptional.isPresent()) {
            log.warn("UpdateVoucherById-[block]:(voucher not found). voucherId:{}, adminId:{}", voucherId, admin.getId());
            throw VoucherException.invalid();
        }

        Voucher voucher = voucherOptional.get();
        voucher.setDiscount(request.getDiscount());
        voucher.setDiscountType(request.getDiscountType());
        voucher.setMaxDiscount(request.getMaxDiscount());
        voucher.setMinOrderPrice(request.getMinOrderPrice());
        voucher.setQuantity(request.getQuantity());
        voucher.setBeginAt(request.getBeginAt());
        voucher.setExpiredAt(request.getExpiredAt());
        voucher.setStatus(request.getStatus());

        if (!ObjectUtils.isEmpty(request.getProduct())) {
            boolean exists = productRepository.existsById(request.getProduct().getId());
            if (!exists) {
                log.warn("UpdateVoucherById-[block]:(product not found). productId:{}, adminId:{}", request.getProduct().getId(), admin.getId());
                throw VoucherException.invalidProduct();
            }
            voucher.setProduct(request.getProduct().toEntity());
        }

        if (!ObjectUtils.isEmpty(request.getCategory())) {
            boolean exists = categoryRepository.existsById(request.getCategory().getId());
            if (!exists) {
                log.warn("UpdateVoucherById-[block]:(category not found). categoryId:{}, adminId:{}", request.getCategory().getId(), admin.getId());
                throw VoucherException.invalidCategory();
            }
            voucher.setCategory(request.getCategory().toEntity());
        }

        Voucher saved = voucherRepository.save(voucher);
        return VoucherDTO.fromEntity(saved);
    }

    public void deleteVoucherById(Long voucherId) throws BaseException {
        Admin admin = authService.getAdmin();

        boolean exists = voucherRepository.existsById(voucherId);
        if (!exists) {
            log.warn("DeleteVoucherById-[block]:(voucher not found). voucherId:{}, adminId:{}", voucherId, admin.getId());
            throw VoucherException.invalid();
        }

        voucherRepository.deleteById(voucherId);
    }
}
