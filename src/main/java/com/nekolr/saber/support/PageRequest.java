package com.nekolr.saber.support;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页请求
 */
@Getter
public class PageRequest {

    /**
     * 页号，第一页为 1
     */
    private int page;

    /**
     * 每页记录数
     */
    private int size;

    /**
     * 排序字段
     */
    @Setter
    private String orderField;

    /**
     * Direction
     */
    private String order;


    public PageRequest() {
        this.page = 1;
        this.size = 10;
    }

    public PageRequest(int page, int size) {
        // 当传递页号小于等于 0 时，默认为第一页
        if (page <= 0) {
            page = 1;
        }

        // 当传递每页记录数小于 1 时，默认为 1
        if (size < 1) {
            size = 1;
        }

        this.page = page;
        this.size = size;
    }

    public void setPage(int page) {
        if (page <= 0) {
            page = 1;
        }
        this.page = page;
    }

    public void setSize(int size) {
        if (size < 1) {
            size = 1;
        }
        this.size = size;
    }

    public void setOrder(String order) {
        if (StringUtils.isNotBlank(order)) {
            if (!"asc".equalsIgnoreCase(order) && !"desc".equalsIgnoreCase(order)) {
                order = "asc";
            }
        }
        this.order = order;
    }

    /**
     * 生成 Pageable
     *
     * @return
     */
    public Pageable toPageable() {
        if (StringUtils.isBlank(this.order) || StringUtils.isBlank(this.orderField)) {
            return org.springframework.data.domain.PageRequest.of(this.page - 1, this.size, Sort.unsorted());
        } else {
            return org.springframework.data.domain.PageRequest.of(this.page - 1, this.size,
                    Sort.by(Sort.Direction.fromString(order), orderField));
        }
    }
}
