package com.natsukashiiz.shop.business;

import co.omise.models.Charge;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.TopUpRequest;
import com.natsukashiiz.shop.model.response.TopUpResponse;
import com.natsukashiiz.shop.model.response.WalletResponse;
import com.natsukashiiz.shop.payment.PaymentService;
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
    private final PaymentService paymentService;

    public WalletResponse myWallet() throws BaseException {
        return walletService.myWallet(authService.getCurrent());
    }

    public WalletResponse create() throws BaseException {
        return walletService.create(authService.getCurrent());
    }

    public TopUpResponse topUp(TopUpRequest request) throws BaseException {
        Account account = authService.getCurrent();
        Charge charge = paymentService.charge(request.getAmount(), request.getSource(), account.getId().toString());
        return TopUpResponse.builder().url(charge.getAuthorizeUri()).build();
//        WalletResponse response = walletService.topUp(request, authService.getCurrent());
    }
}
