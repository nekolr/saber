package com.nekolr.saber.controller;

import com.nekolr.saber.service.ImageService;
import com.nekolr.saber.service.dto.ImageDTO;
import com.nekolr.saber.service.query.ImageQueryService;
import com.nekolr.saber.support.PageRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Image controller
 */
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ImageController {


    private final ImageService imageService;
    private final ImageQueryService imageQueryService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseEntity<@NonNull String> upload(@RequestParam(name = "image") MultipartFile image) {
        return ResponseEntity.ok(imageService.saveImage(image));
    }

    /**
     * 获取图片列表，带分页
     */
    @GetMapping("/images")
    public ResponseEntity<@NonNull Page<@NonNull ImageDTO>> getImages(ImageDTO image, PageRequest pageRequest) {
        return ResponseEntity.ok(imageQueryService.queryAll(image, pageRequest.toPageable()));
    }

    /**
     * 删除图片
     */
    @DeleteMapping("/images")
    public ResponseEntity<@NonNull Void> deleteImage(Long id) {
        imageService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
