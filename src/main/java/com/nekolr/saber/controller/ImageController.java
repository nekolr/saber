package com.nekolr.saber.controller;

import com.nekolr.saber.service.ImageService;
import com.nekolr.saber.service.dto.ImageDTO;
import com.nekolr.saber.service.query.ImageQueryService;
import com.nekolr.saber.support.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Image controller
 *
 * @author nekolr
 */
@RestController
@RequestMapping("api")
public class ImageController {

    @Resource
    private ImageService imageService;
    @Resource
    private ImageQueryService imageQueryService;

    /**
     * 上传文件
     *
     * @param image
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam(name = "image") MultipartFile image) {
        return ResponseEntity.ok(imageService.saveImage(image));
    }

    /**
     * 获取图片列表，带分页
     *
     * @param image
     * @param pageRequest
     * @return
     */
    @GetMapping("/images")
    public ResponseEntity<Page<ImageDTO>> getImages(ImageDTO image, PageRequest pageRequest) {
        return ResponseEntity.ok(imageQueryService.queryAll(image, pageRequest.toPageable()));
    }

    /**
     * 删除图片
     *
     * @return
     */
    @DeleteMapping("/images")
    public ResponseEntity deleteImage(Long id) {
        imageService.deleteImage(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
