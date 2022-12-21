package com.nekolr.saber.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Image Entity
 *
 * @author nekolr
 */
@Getter
@Setter
@Entity
@Table(name = "image")
public class Image implements Serializable {

    /**
     * ID
     */
    @Id
    private Long id;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 源文件名
     */
    @Column(name = "origin_name")
    private String originName;

    /**
     * 上传处理后的文件名
     */
    @Column(name = "short_name")
    private String shortName;

    /**
     * 文件大小（单位是 Byte）
     */
    private Long size;

    /**
     * 用户
     */
    @ManyToOne(targetEntity = User.class)
    private User user;

    /**
     * 重写 toString
     *
     * @return
     */
    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", deleted=" + deleted +
                ", originName='" + originName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", size=" + size +
                '}';
    }
}
