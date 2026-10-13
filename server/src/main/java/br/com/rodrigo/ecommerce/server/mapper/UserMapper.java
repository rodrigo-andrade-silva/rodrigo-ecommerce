package br.com.rodrigo.ecommerce.server.mapper;

import br.com.rodrigo.ecommerce.server.dto.UserDTO;
import br.com.rodrigo.ecommerce.server.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO dto);

    UserDTO toDto(User entity);
}