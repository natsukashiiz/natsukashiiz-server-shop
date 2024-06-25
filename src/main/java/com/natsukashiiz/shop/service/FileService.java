package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.FileException;
import com.natsukashiiz.shop.model.response.FileStoreRequest;
import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Log4j2
@AllArgsConstructor
public class FileService {

    private final Path rootLocation = Paths.get("uploads");
    private final AuthService authService;

    public String store(MultipartFile file) throws BaseException {
//        MultipartFile file = request.getFile();

//        if (!ObjectUtils.isEmpty(request.getType())) {
//            // concat upload location with user id and type
//            this.rootLocation.resolve(
//                    Paths.get(
//                            String.valueOf(authService.getCurrent().getId()),
//                            String.valueOf(request.getType()).toLowerCase()
//                    )
//            );
//        }

        try {
            if (file.isEmpty()) {
                throw FileException.emptyFile();
            }
            if (file.getOriginalFilename() == null) {
                throw FileException.invalidName();
            }
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

//            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
//            log.debug("Mime type: {}", mimeType);
//            if (mimeType == null) {
//                throw FileException.invalidType();
//            }

            String fileType = "jpg";
            String newFileName = RandomUtils.notSymbol() + "." + fileType;

            Path destinationFile = this.rootLocation.resolve(Paths.get(newFileName)).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw FileException.invalidPath();
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return newFileName;
        } catch (IOException e) {
            log.warn("Store-[unknown].", e);
            throw FileException.unknown();
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
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

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
