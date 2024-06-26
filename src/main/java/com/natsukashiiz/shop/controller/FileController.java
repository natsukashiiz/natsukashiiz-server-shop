package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/files")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<?> store(MultipartFile file) throws BaseException {
        return ResponseEntity.ok(fileService.store(file));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> load(@PathVariable String fileName) throws BaseException {
        Resource file = fileService.loadAsResource(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(file);
    }
}
