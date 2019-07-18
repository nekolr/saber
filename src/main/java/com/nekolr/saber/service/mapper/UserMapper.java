package com.nekolr.saber.service.mapper;

import com.nekolr.saber.entity.User;
import com.nekolr.saber.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<User, UserDTO> {

}
