package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.VoucherStatus;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.AccountVoucher;
import com.natsukashiiz.shop.entity.Voucher;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.VoucherException;
import com.natsukashiiz.shop.model.response.VoucherResponse;
import com.natsukashiiz.shop.repository.AccountVoucherRepository;
import com.natsukashiiz.shop.repository.VoucherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final AuthService authService;
    private final AccountVoucherRepository accountVoucherRepository;

    public List<VoucherResponse> queryVouchers() throws BaseException {
        List<Voucher> vouchers = voucherRepository.findAll();
        List<VoucherResponse> responses = VoucherResponse.buildList(vouchers);

        if (!authService.anonymous()) {
            Account account = authService.getCurrent();
            accountVoucherRepository.findByAccount(account)
                    .stream()
                    .map(e -> e.getVoucher().getId())
                    .forEach(id -> responses.stream().filter(e -> e.getId().equals(id)).forEach(e -> e.setClaimed(Boolean.TRUE)));
        }

        return responses;
    }

    public VoucherResponse queryVoucherById(Long voucherId) throws BaseException {
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(VoucherException::invalid);
        return VoucherResponse.build(voucher);
    }

    @Transactional
    public void claimVoucher(Long voucherId) throws BaseException {
        Account account = authService.getCurrent();
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(VoucherException::invalid);

        if (accountVoucherRepository.existsByVoucherAndAccount(voucher, account)) {
            throw VoucherException.alreadyClaimed();
        }

        if (VoucherStatus.INACTIVE.equals(voucher.getStatus())) {
            throw VoucherException.notAvailable();
        }

        if (voucher.getQuantity() <= 0) {
            throw VoucherException.notEnough();
        }

        if (voucher.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw VoucherException.expired();
        }

        voucher.setQuantity(voucher.getQuantity() - 1);
        voucherRepository.save(voucher);

        AccountVoucher accountVoucher = new AccountVoucher();
        accountVoucher.setVoucher(voucher);
        accountVoucher.setAccount(account);
        accountVoucher.setUsed(Boolean.FALSE);
        accountVoucherRepository.save(accountVoucher);
    }
}
