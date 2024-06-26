package com.natsukashiiz.shop.exception;

public class FileException extends BaseException {

    public FileException(String code) {
        super("file." + code);
    }

    public static FileException emptyFile() {
        return new FileException("empty");
    }

    public static FileException invalidPath() {
        return new FileException("invalid.path");
    }

    public static FileException unknown() {
        return new FileException("unknown");
    }

    public static BaseException invalidName() {
        return new FileException("invalid.name");
    }

    public static BaseException notFound() {
        return new FileException("not.found");
    }

    public static BaseException invalidType() {
        return new FileException("invalid.type");
    }

    public static BaseException typeNotSupported() {
        return new FileException("type.not.supported");
    }
}
