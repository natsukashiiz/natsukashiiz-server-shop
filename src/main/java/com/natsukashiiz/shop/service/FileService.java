package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.ApiProperties;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.FileException;
import com.natsukashiiz.shop.model.response.FileStoreResponse;
import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Log4j2
@AllArgsConstructor
public class FileService {

    private final ApiProperties properties;

    public FileStoreResponse store(MultipartFile file) throws BaseException {
        log.debug("Store-[next]. file:{}", file);

        try {
            if (file.isEmpty()) {
                log.warn("Store-[block]:(file is empty). file:{}", file);
                throw FileException.emptyFile();
            }
            if (ObjectUtils.isEmpty(file.getOriginalFilename())) {
                log.warn("Store-[block]:(file name is empty). file:{}", file);
                throw FileException.invalidName();
            }
            if (ObjectUtils.isEmpty(file.getContentType())) {
                log.warn("Store-[block]:(file type is empty). file:{}", file);
                throw FileException.typeNotSupported();
            }
            if (!properties.getFileAllowedTypes().contains(file.getContentType())) {
                log.warn("Store-[block]:(file type not supported). file:{}", file);
                throw FileException.typeNotSupported();
            }

            Path rootLocation = properties.getFileUploadDir();
            if (!Files.exists(rootLocation)) {
                log.debug("Store-[next]:(create directory). rootLocation:{}", rootLocation);
                Files.createDirectories(rootLocation);
            }

            String fileType = file.getContentType().split("/")[1];
            String newFileName = RandomUtils.notSymbol() + "." + fileType;

            Path destinationFile = rootLocation.resolve(Paths.get(newFileName)).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                log.warn("Store-[block]:(invalid path). destinationFile:{}", destinationFile);
                throw FileException.unknown();
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            FileStoreResponse response = new FileStoreResponse();
            response.setName(newFileName);
            response.setType(fileType);
            response.setUrl(properties.getFileBaseUrl() + newFileName);
            response.setSize(file.getSize());

            return response;
        } catch (IOException e) {
            log.warn("Store-[unknown].", e);
            throw FileException.unknown();
        }
    }

    public Path load(String filename) {
        return properties.getFileUploadDir().resolve(filename);
    }

    public Resource loadAsResource(String filename) throws BaseException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw FileException.notFound();
            }
        } catch (MalformedURLException e) {
            log.warn("LoadAsResource-[unknown].", e);
            throw FileException.unknown();
        }
    }

    public void delete(String filename) throws BaseException {
        try {
            Path file = loadAsResource(filename).getFile().toPath();
            Files.delete(file);
        } catch (IOException e) {
            log.warn("Delete-[unknown].", e);
            throw FileException.unknown();
        }
    }

    public void deleteWithUrl(String url) throws BaseException {
        try {
            String[] split = url.split("/");
            String filename = split[split.length - 1];
            delete(filename);
        } catch (Exception e) {
            log.warn("DeleteWithUrl-[unknown].", e);
            throw FileException.unknown();
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(properties.getFileUploadDir().toFile());
    }
}
