package com.nekolr.saber.service.query;

import com.nekolr.saber.dao.ImageRepository;
import com.nekolr.saber.entity.Image;
import com.nekolr.saber.entity.User;
import com.nekolr.saber.service.dto.ImageDTO;
import com.nekolr.saber.service.dto.UserDTO;
import com.nekolr.saber.service.mapper.ImageMapper;
import com.nekolr.saber.service.mapper.UserMapper;
import com.nekolr.saber.support.ContextHolder;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@NullMarked
@AllArgsConstructor
@CacheConfig(cacheNames = "image")
public class ImageQueryService {

    private final UserMapper userMapper;
    private final ImageMapper imageMapper;
    private final ContextHolder contextHolder;
    private final ImageRepository imageRepository;

    @Cacheable(keyGenerator = "keyGenerator")
    public Page<ImageDTO> queryAll(ImageDTO imageDto, Pageable pageable) {
        UserDTO currentUser = contextHolder.getCurrentUser();
        User user = userMapper.toEntity(currentUser);

        // 创建查询示例
        Image probe = new Image();
        probe.setId(imageDto.getId());
        probe.setUser(user);
        probe.setDeleted(false);

        Example<Image> example = Example.of(probe);
        return imageRepository.findAll(example, pageable).map(imageMapper::toDto);
    }
}
