package com.natsukashiiz.shop.api.service;

import com.natsukashiiz.shop.common.VoucherStatus;
import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.UserVoucher;
import com.natsukashiiz.shop.entity.Voucher;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.VoucherException;
import com.natsukashiiz.shop.api.model.response.VoucherResponse;
import com.natsukashiiz.shop.repository.UserVoucherRepository;
import com.natsukashiiz.shop.repository.VoucherRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final AuthService authService;
    private final UserVoucherRepository userVoucherRepository;

    public List<VoucherResponse> queryAllVoucher() throws BaseException {

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.exact());

        Voucher search = new Voucher();
        search.setStatus(VoucherStatus.ACTIVE);

        Example<Voucher> example = Example.of(search, matcher);
        List<Voucher> vouchers = voucherRepository.findAll(example);
        List<VoucherResponse> responses = VoucherResponse.buildList(vouchers);

        if (!authService.anonymous()) {
            User user = authService.getUser();
            userVoucherRepository.findAllByUser(user)
                    .stream()
                    .map(e -> e.getVoucher().getId())
                    .forEach(id -> responses.stream().filter(e -> e.getId().equals(id)).forEach(e -> e.setClaimed(Boolean.TRUE)));
        }

        return responses;
    }

    public VoucherResponse queryVoucherById(Long voucherId) throws BaseException {
        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        if (!voucherOptional.isPresent()) {
            log.warn("QueryVoucherById-[block]:(voucher not found). voucherId:{}", voucherId);
            throw VoucherException.invalid();
        }
        return VoucherResponse.build(voucherOptional.get());
    }

    @Transactional
    public void claimVoucher(Long voucherId) throws BaseException {
        User user = authService.getUser();

        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        if (!voucherOptional.isPresent()) {
            log.warn("ClaimVoucher-[block]:(voucher not found). voucherId:{}, accountId:{}", voucherId, user.getId());
            throw VoucherException.invalid();
        }

        Voucher voucher = voucherOptional.get();

        if (userVoucherRepository.existsByVoucherAndUser(voucher, user)) {
            log.warn("ClaimVoucher-[block]:(voucher already claimed). voucherId:{}, accountId:{}", voucherId, user.getId());
            throw VoucherException.alreadyClaimed();
        }

        if (VoucherStatus.INACTIVE.equals(voucher.getStatus())) {
            log.warn("ClaimVoucher-[block]:(voucher not available). voucherId:{}, accountId:{}", voucherId, user.getId());
            throw VoucherException.notAvailable();
        }

        if (voucher.getQuantity() <= 0) {
            log.warn("ClaimVoucher-[block]:(voucher not enough). voucherId:{}, accountId:{}", voucherId, user.getId());
            throw VoucherException.notEnough();
        }

        if (voucher.getExpiredAt().isBefore(LocalDateTime.now())) {
            log.warn("ClaimVoucher-[block]:(voucher expired). voucherId:{}, accountId:{}", voucherId, user.getId());
            throw VoucherException.expired();
        }

        voucher.setQuantity(voucher.getQuantity() - 1);

        if (voucher.getQuantity() <= 0) {
            voucher.setStatus(VoucherStatus.INACTIVE);
        }

        voucherRepository.save(voucher);

        UserVoucher userVoucher = new UserVoucher();
        userVoucher.setVoucher(voucher);
        userVoucher.setUser(user);
        userVoucher.setUsed(Boolean.FALSE);
        userVoucherRepository.save(userVoucher);
    }
}
