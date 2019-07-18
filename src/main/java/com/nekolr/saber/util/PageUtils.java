package com.nekolr.saber.util;

import com.nekolr.saber.support.PageVO;
import org.springframework.data.domain.Page;

/**
 * 分页工具类
 */
public class PageUtils {

    /**
     * 将分页数据转换
     *
     * @param page
     * @return
     */
    public static PageVO toPageVO(Page page) {
        return new PageVO(page.getContent(), page.getTotalElements());
    }
}
