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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
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
@CacheConfig(cacheNames = "image")
public class ImageServiceImpl implements ImageService {

    private final Hashids hashids;
    private final I18nUtils i18nUtils;
    private final UserMapper userMapper;
    private final IdSeqService idSeqService;
    private final StorageService storageService;
    private final ImageRepository imageRepository;
    private final MySecurityContextHolder securityContextHolder;

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public String saveImage(MultipartFile image) {
        this.validateImageFile(image);

        String originName = image.getOriginalFilename();
        InputStream inputStream = null;

        try {
            inputStream = this.wrapper(image.getInputStream());

            // 解析文件信息
            FileMetadata metadata = resolveFileMetadata(inputStream, image, originName);

            // 生成唯一序列值和文件名
            Long id = idSeqService.save(new IdSeq()).getId();
            String filename = this.generateFilename(id, metadata.suffix);

            // 上传文件
            String shortName = storageService.upload(inputStream, filename, metadata.contentType, image.getSize());

            // 保存文件信息到数据库
            saveImageEntity(id, originName, shortName, image.getSize());

            return shortName;

        } catch (IOException e) {
            throw new RuntimeException(i18nUtils.getMessage("exceptions.upload_file_failed"), e);
        } finally {
            closeStream(inputStream);
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
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void deleteImage(Long id) {
        imageRepository.findById(id).ifPresent(image -> {
            // 逻辑删除
            image.setDeleted(true);
            imageRepository.save(image);
        });
    }

    /**
     * 验证图片文件
     */
    private void validateImageFile(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException(i18nUtils.getMessage("exceptions.file_empty"));
        }
        if (image.getSize() <= 0) {
            throw new IllegalArgumentException(i18nUtils.getMessage("exceptions.file_size_invalid"));
        }
    }

    /**
     * 解析文件元数据
     */
    private FileMetadata resolveFileMetadata(InputStream inputStream, MultipartFile image, String originName) throws IOException {
        // 通过文件头获取文件后缀
        String suffix = this.getFileSuffix(inputStream);
        // 处理文件 MIME 类型
        String contentType = this.resolveContentType(image);
        // 处理 svg 文件
        suffix = this.judgeSvgFile(suffix, contentType);
        if (Objects.isNull(suffix)) {
            suffix = getExtName(originName);
        }

        return new FileMetadata(suffix, contentType);
    }

    /**
     * 保存图片实体信息
     */
    private void saveImageEntity(Long id, String originName, String shortName, Long size) {
        Image entity = new Image();
        entity.setId(id);
        entity.setOriginName(originName);
        entity.setDeleted(false);
        entity.setShortName(shortName);
        entity.setSize(size);
        entity.setUser(userMapper.toEntity(securityContextHolder.getCurrentUser()));

        // 持久化文件信息到数据库
        imageRepository.save(entity);
    }

    /**
     * 安全关闭流
     */
    private void closeStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // 记录日志或忽略关闭异常
            }
        }
    }

    /**
     * 文件元数据内部类
     */
    private static class FileMetadata {
        String suffix;
        String contentType;

        FileMetadata(String suffix, String contentType) {
            this.suffix = suffix;
            this.contentType = contentType;
        }
    }
}
