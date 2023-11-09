package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.response.WalletResponse;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class WalletBusiness {
    private final WalletService walletService;
    private final AuthService authService;

    public WalletResponse myWallet() throws BaseException {
        return walletService.myWallet(authService.getCurrent());
    }

    public WalletResponse create() throws BaseException {
        return walletService.create(authService.getCurrent());
    }
}
