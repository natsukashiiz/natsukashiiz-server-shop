package com.natsukashiiz.shop.exception;

public class WalletException extends BaseException {

    public WalletException(String code) {
        super("wallet." + code);
    }

    public static WalletException invalid() {
        return new WalletException("invalid");
    }

    public static WalletException insufficient() {
        return new WalletException("balance.insufficient");
    }
}
