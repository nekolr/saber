package com.nekolr.saber.service.mapper;

import com.nekolr.saber.entity.Image;
import com.nekolr.saber.service.dto.ImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper extends EntityMapper<Image, ImageDTO> {

}
