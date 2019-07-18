package com.nekolr.saber.service.impl;

import com.nekolr.saber.dao.ImageRepository;
import com.nekolr.saber.entity.Image;
import com.nekolr.saber.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteImage(Long id) {
        imageRepository.findById(id).ifPresent(image -> {
            // 逻辑删除
            image.setDeleted(true);
            imageRepository.save(image);
        });
    }
}
