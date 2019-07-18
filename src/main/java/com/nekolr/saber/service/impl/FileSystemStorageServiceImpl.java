package com.nekolr.saber.service.impl;

import com.nekolr.saber.service.StorageService;
import com.nekolr.saber.util.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 只操作文件系统
 *
 * @author nekolr
 */
@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "filesystem")
public class FileSystemStorageServiceImpl implements StorageService {

    @Value("${storage.filesystem.imgFolder}")
    private String imgFolder;

    @Override
    public String upload(InputStream in, String filename, String contentType, long length) throws IOException {
        String savePath = imgFolder;
        Path targetPath = new File(getParentPath(savePath), filename).toPath();
        Files.copy(in, targetPath);
        return DateUtils.currentDate() + "/" + filename;
    }

    @Override
    public void delete(String filename) {
        String filepath = imgFolder + File.separator + filename;
        File f = new File(filepath);
        if (f.exists()) {
            f.delete();
        }
    }

    /**
     * 获取文件存储的目录，目录不存在时会自动创建
     * <p>
     * example: 2019\06\22
     *
     * @return
     */
    private String getParentPath(String savePath) {
        String parent = savePath + File.separator + DateUtils.currentSaveDate();
        File f = new File(parent);
        if (!f.exists()) {
            f.mkdirs();
        }
        return parent;
    }
}
