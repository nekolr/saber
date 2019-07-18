package com.nekolr.saber.service.mapper;

import com.nekolr.saber.entity.IdSeq;
import com.nekolr.saber.service.dto.IdSeqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IdSeqMapper extends EntityMapper<IdSeq, IdSeqDTO> {

}
