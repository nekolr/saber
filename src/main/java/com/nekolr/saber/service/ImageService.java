package com.nekolr.saber.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.multipart.MultipartFile;

@CacheConfig(cacheNames = "image")
public interface ImageService {

    /**
     * 保存 Image
     *
     * @return
     */
    @CacheEvict(allEntries = true)
    String saveImage(MultipartFile image);

    /**
     * 删除 Image
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void deleteImage(Long id);

}
