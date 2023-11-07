package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.response.WalletResponse;
import com.natsukashiiz.shop.service.WalletService;
import com.natsukashiiz.shop.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class WalletBusiness {
    private final WalletService walletService;

    public WalletResponse myWallet() throws BaseException {
        return walletService.myWallet(SecurityUtils.getAuth());
    }

    public WalletResponse create() throws BaseException {
        return walletService.create(SecurityUtils.getAuth());
    }
}
