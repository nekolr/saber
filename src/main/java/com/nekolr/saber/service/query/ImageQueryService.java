package com.nekolr.saber.service.query;

import com.nekolr.saber.dao.ImageRepository;
import com.nekolr.saber.entity.Image;
import com.nekolr.saber.entity.User;
import com.nekolr.saber.service.dto.ImageDTO;
import com.nekolr.saber.service.mapper.ImageMapper;
import com.nekolr.saber.service.mapper.UserMapper;
import com.nekolr.saber.support.PageVO;
import com.nekolr.saber.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@CacheConfig(cacheNames = "image")
public class ImageQueryService {

    @Autowired
    private ImageRepository imageRepository;
    @Resource
    private ImageMapper imageMapper;
    @Resource
    private UserMapper userMapper;

    @Cacheable(keyGenerator = "keyGenerator")
    public PageVO queryAll(ImageDTO image, Pageable pageable) {
        Page<Image> users = imageRepository.findAll(new Spec(image), pageable);
        return PageUtils.toPageVO(users.map(imageMapper::toDto));
    }

    class Spec implements Specification<Image> {

        private ImageDTO image;

        public Spec(ImageDTO image) {
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
                predicates.add(cb.equal(root.get("user").as(User.class), userMapper.toEntity(image.getUser())));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        }
    }
}
