package com.nekolr.saber.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    /**
     * 保存 Image
     */
    String saveImage(MultipartFile image);

    /**
     * 删除 Image
     */
    void deleteImage(Long id);

}
