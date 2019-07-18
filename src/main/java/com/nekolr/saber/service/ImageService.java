package com.nekolr.saber.service;


import com.nekolr.saber.entity.Image;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;

@CacheConfig(cacheNames = "image")
public interface ImageService {

    /**
     * 保存 Image
     *
     * @return
     */
    @CacheEvict(allEntries = true)
    Image saveImage(Image image);

    /**
     * 删除 Image
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void deleteImage(Long id);

}
