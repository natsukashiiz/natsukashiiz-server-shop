package com.natsukashiiz.shop.admin.service;

import com.natsukashiiz.shop.admin.model.dto.VoucherDTO;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Admin;
import com.natsukashiiz.shop.entity.Voucher;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.VoucherException;
import com.natsukashiiz.shop.model.resposne.PageResponse;
import com.natsukashiiz.shop.repository.VoucherRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final AuthService authService;

    public PageResponse<List<VoucherDTO>> queryAllCategories(VoucherDTO request, PaginationRequest pagination) {
        Example<Voucher> example = Example.of(request.toEntity(), ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase());
        Page<Voucher> page = voucherRepository.findAll(example, pagination);
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

        if (ObjectUtils.isEmpty(request.getCode())) {
            log.warn("CreateVoucher-[block]:(code is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidCode();
        }

        if (ObjectUtils.isEmpty(request.getDiscount())) {
            log.warn("CreateVoucher-[block]:(discount is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidDiscount();
        }

        if (ObjectUtils.isEmpty(request.getDiscountType())) {
            log.warn("CreateVoucher-[block]:(discountType is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidDiscountType();
        }

        if (ObjectUtils.isEmpty(request.getMaxDiscount())) {
            log.warn("CreateVoucher-[block]:(maxDiscount is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidMaxDiscount();
        }

        if (ObjectUtils.isEmpty(request.getMinOrderPrice())) {
            log.warn("CreateVoucher-[block]:(minOrderPrice is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidMinOrderPrice();
        }

        if (ObjectUtils.isEmpty(request.getQuantity())) {
            log.warn("CreateVoucher-[block]:(quantity is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidQuantity();
        }

        if (ObjectUtils.isEmpty(request.getProduct())) {
            log.warn("CreateVoucher-[block]:(product is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidProduct();
        }

        if (ObjectUtils.isEmpty(request.getCategory())) {
            log.warn("CreateVoucher-[block]:(category is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidCategory();
        }

        if (ObjectUtils.isEmpty(request.getBeginAt())) {
            log.warn("CreateVoucher-[block]:(beginAt is empty). adminId:{}", admin.getId());
            throw VoucherException.invalidBeginAt();
        }

        if (ObjectUtils.isEmpty(request.getExpiredAt())) {
            log.warn("CreateVoucher-[block]:(expiredAt is empty). adminId:{}", admin.getId());
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
        voucher.setCode(request.getCode());
        voucher.setDiscount(request.getDiscount());
        voucher.setDiscountType(request.getDiscountType());
        voucher.setMaxDiscount(request.getMaxDiscount());
        voucher.setMinOrderPrice(request.getMinOrderPrice());
        voucher.setQuantity(request.getQuantity());
        voucher.setProduct(request.getProduct().toEntity());
        voucher.setCategory(request.getCategory().toEntity());
        voucher.setBeginAt(request.getBeginAt());
        voucher.setExpiredAt(request.getExpiredAt());

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
