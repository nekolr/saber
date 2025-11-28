package com.nekolr.saber.service.mapper;


import java.util.List;

/**
 * 实体映射接口
 *
 * @param <E> entity 类型
 * @param <D> dto 类型
 */
public interface EntityMapper<E, D> {

    /**
     * 将 entity 映射为 dto
     */
    D toDto(E entity);

    /**
     * 将 dto 映射为 entity
     */
    E toEntity(D dto);

    /**
     * entity 结合映射为 dto 集合
     */
    List<D> toDto(List<E> entityList);

    /**
     * dto 集合映射为 entity 集合
     */
    List<E> toEntity(List<D> dtoList);
}
