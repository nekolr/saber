package com.nekolr.saber.controller;

import cn.hutool.core.io.FileTypeUtil;
import com.nekolr.saber.entity.IdSeq;
import com.nekolr.saber.entity.Image;
import com.nekolr.saber.exception.ErrorResponse;
import com.nekolr.saber.service.IdSeqService;
import com.nekolr.saber.service.ImageService;
import com.nekolr.saber.service.StorageService;
import com.nekolr.saber.service.dto.ImageDTO;
import com.nekolr.saber.service.mapper.UserMapper;
import com.nekolr.saber.service.query.ImageQueryService;
import com.nekolr.saber.support.I18nUtils;
import com.nekolr.saber.support.PageRequest;
import com.nekolr.saber.support.SecurityContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Image controller
 *
 * @author nekolr
 */
@RestController
@RequestMapping("api")
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private IdSeqService idSeqService;
    @Autowired
    private SecurityContextHolder securityContextHolder;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private Hashids hashids;
    @Autowired
    private I18nUtils i18nUtils;
    @Autowired
    private ImageQueryService imageQueryService;

    /**
     * 上传文件
     *
     * @param image
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam(name = "image") MultipartFile image) {

        String originName = image.getOriginalFilename();
        String shortName;
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

            Long id = idSeqService.save(new IdSeq()).getId();

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
            // 生成文件名
            String filename = hashids.encode(id);
            if (StringUtils.isNotBlank(suffix)) {
                filename = filename + "." + suffix;
            }
            // 将文件上传
            shortName = storageService.upload(markSupportStream, filename, contentType, size);

            Image entity = new Image();
            entity.setId(id);
            entity.setOriginName(originName);
            entity.setDeleted(false);
            entity.setShortName(shortName);
            entity.setSize(size);
            entity.setUser(userMapper.toEntity(securityContextHolder.getUser()));

            // 持久化文件信息到数据库
            imageService.saveImage(entity);

            return ResponseEntity.ok(shortName);

        } catch (IOException e) {
            e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.value(), i18nUtils.getMessage("exceptions.upload_file_failed"));
            return new ResponseEntity(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
        }
    }

    /**
     * 获取图片列表，带分页
     *
     * @param image
     * @param pageRequest
     * @return
     */
    @GetMapping("/images")
    public ResponseEntity getImages(ImageDTO image, PageRequest pageRequest) {
        // 只能获取自己的图片
        image.setUser(securityContextHolder.getUser());
        image.setDeleted(false);
        return ResponseEntity.ok(imageQueryService.queryAll(image, pageRequest.toPageable()));
    }

    /**
     * 删除图片
     *
     * @return
     */
    @DeleteMapping("/images")
    public ResponseEntity deleteImage(Long id) {
        imageService.deleteImage(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
