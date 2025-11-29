package com.nekolr.saber.service.query;

import com.nekolr.saber.dao.ImageRepository;
import com.nekolr.saber.entity.Image;
import com.nekolr.saber.service.dto.ImageDTO;
import com.nekolr.saber.service.mapper.ImageMapper;
import com.nekolr.saber.support.MySecurityContextHolder;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@NullMarked
@AllArgsConstructor
@CacheConfig(cacheNames = "image")
public class ImageQueryService {

    private final ImageMapper imageMapper;
    private final ImageRepository imageRepository;
    private final MySecurityContextHolder mySecurityContextHolder;

    @Cacheable(keyGenerator = "keyGenerator")
    public Page<ImageDTO> queryAll(ImageDTO image, Pageable pageable) {
        // 只能获取自己的图片
        image.setUser(mySecurityContextHolder.getCurrentUser());
        image.setDeleted(false);
        return imageRepository.findAll(new Spec(imageMapper.toEntity(image)), pageable).map(imageMapper::toDto);
    }

    static class Spec implements Specification<Image> {

        private final Image image;

        public Spec(Image image) {
            this.image = image;
        }

        @Override
        public Predicate toPredicate(Root<Image> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(image.getId())) {
                // id 相等
                predicates.add(cb.equal(root.get("id").as(Long.class), image.getId()));
            }

            if (Objects.nonNull(image.getDeleted())) {
                predicates.add(cb.equal(root.get("deleted").as(Boolean.class), image.getDeleted()));
            }

            if (Objects.nonNull(image.getUser())) {
                predicates.add(cb.equal(root.get("user"), image.getUser()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }
    }
}
