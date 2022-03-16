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
import java.util.Arrays;
import java.util.List;

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

    /**
     * TODO refactor
     *
     * @param image
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveImage(MultipartFile image) {
        String originName = image.getOriginalFilename();
        try {
            InputStream in = image.getInputStream();
            // 返回支持 mark 的流
            InputStream markSupportStream = new BufferedInputStream(in);
            // 标记
            markSupportStream.mark(28);
            // 读取文件头部的 28 个字节判断文件类型
            String suffix = FileTypeUtil.getType(markSupportStream);
            // 重置流
            markSupportStream.reset();

            // 获取文件大小
            long size = image.getSize();
            // 文件 MINE 类型处理
            String contentType = image.getContentType();
            if (StringUtils.isBlank(contentType)) {
                contentType = URLConnection.guessContentTypeFromName(originName);
            }
            if (StringUtils.isBlank(contentType)) {
                contentType = "image/png";
            }
            // 有时候文件头比较特殊，比如 svg，此时使用上传文件后缀作为文件格式
            if (StringUtils.isBlank(suffix)) {
                List<String> partsOfName = Arrays.asList(StringUtils.split(originName, "."));
                if (partsOfName.size() != 1) {
                    suffix = partsOfName.get(partsOfName.size() - 1);
                }
            }

            Long id = idSeqService.save(new IdSeq()).getId();
            // 生成文件名
            String filename = hashids.encode(id);
            if (StringUtils.isNotBlank(suffix)) {
                filename = filename + "." + suffix;
            }
            // 将文件上传
            String shortName = storageService.upload(markSupportStream, filename, contentType, size);

            Image entity = new Image();
            entity.setId(id);
            entity.setOriginName(originName);
            entity.setDeleted(false);
            entity.setShortName(shortName);
            entity.setSize(size);
            entity.setUser(userMapper.toEntity(securityContextHolder.getCurrentUser()));

            // 持久化文件信息到数据库
            imageRepository.save(entity);

            return shortName;

        } catch (IOException e) {
            throw new RuntimeException(i18nUtils.getMessage("exceptions.upload_file_failed"));
        }
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
