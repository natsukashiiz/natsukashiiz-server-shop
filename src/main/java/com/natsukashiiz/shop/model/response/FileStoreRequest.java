package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.common.FileStoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class FileStoreRequest {
    private MultipartFile file;
    private FileStoreType type;
}
