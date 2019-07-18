package com.nekolr.saber.service.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Image DTO
 */
@Data
public class ImageDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 是否删除
     */
    @JsonIgnore
    private Boolean deleted;

    /**
     * 源文件名
     */
    private String originName;

    /**
     * 上传处理后的文件名
     */
    private String shortName;

    /**
     * 文件大小（单位是 Byte）
     */
    private Long size;

    /**
     * 用户
     */
    @JsonIgnore
    private UserDTO user;
}
