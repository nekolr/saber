package com.nekolr.saber.service.impl;

import com.nekolr.saber.dao.ImageRepository;
import com.nekolr.saber.entity.IdSeq;
import com.nekolr.saber.entity.Image;
import com.nekolr.saber.service.IdSeqService;
import com.nekolr.saber.service.ImageService;
import com.nekolr.saber.service.StorageService;
import com.nekolr.saber.service.mapper.UserMapper;
import com.nekolr.saber.support.I18nUtils;
import com.nekolr.saber.support.MySecurityContextHolder;
import com.nekolr.saber.util.FileTypeUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Hashids hashids;
    private final I18nUtils i18nUtils;
    private final UserMapper userMapper;
    private final IdSeqService idSeqService;
    private final StorageService storageService;
    private final ImageRepository imageRepository;
    private final MySecurityContextHolder securityContextHolder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveImage(MultipartFile image) {
        String originName = image.getOriginalFilename();
        try {
            InputStream inputStream = this.wrapper(image.getInputStream());
            // 通过文件头获取文件后缀
            String suffix = this.getFileSuffix(inputStream);
            // 处理文件 MIME 类型
            String contentType = this.resolveContentType(image);
            // 处理 svg 文件
            suffix = this.judgeSvgFile(suffix, contentType);
            if (Objects.isNull(suffix)) {
                suffix = getExtName(originName);
            }
            // 生成唯一序列值
            Long id = idSeqService.save(new IdSeq()).getId();
            // 生成文件名
            String filename = this.generateFilename(id, suffix);
            // 将文件上传
            String shortName = storageService.upload(inputStream, filename, contentType, image.getSize());

            Image entity = new Image();
            entity.setId(id);
            entity.setOriginName(originName);
            entity.setDeleted(false);
            entity.setShortName(shortName);
            entity.setSize(image.getSize());
            entity.setUser(userMapper.toEntity(securityContextHolder.getCurrentUser()));

            // 持久化文件信息到数据库
            imageRepository.save(entity);

            return shortName;

        } catch (IOException e) {
            throw new RuntimeException(i18nUtils.getMessage("exceptions.upload_file_failed"));
        }
    }

    private String generateFilename(Long id, String suffix) {
        // 生成文件名
        String filename = hashids.encode(id);
        if (StringUtils.isNotBlank(suffix)) {
            filename = filename + "." + suffix;
        }
        return filename;
    }

    private InputStream wrapper(InputStream inputStream) {
        return new BufferedInputStream(inputStream);
    }

    private String getFileSuffix(InputStream markSupportStream) throws IOException {
        // 标记
        markSupportStream.mark(28);
        // 读取文件头部的 28 个字节判断文件类型
        String suffix = FileTypeUtil.getType(markSupportStream);
        // 重置流
        markSupportStream.reset();

        return suffix;
    }

    private String getExtName(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return null;
        }
        final int index = filename.lastIndexOf(".");
        if (index == -1) {
            return null;
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private String resolveContentType(MultipartFile file) {
        String originName = file.getOriginalFilename();
        String contentType = file.getContentType();
        if (StringUtils.isBlank(contentType)) {
            contentType = URLConnection.guessContentTypeFromName(originName);
        }
        return contentType;
    }

    private String judgeSvgFile(String suffix, String contentType) {
        if ("xml".equals(suffix) && "image/svg+xml".equals(contentType)) {
            return "svg";
        }
        return suffix;
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
