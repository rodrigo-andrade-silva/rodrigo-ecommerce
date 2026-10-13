package br.com.rodrigo.ecommerce.server.mapper;

import br.com.rodrigo.ecommerce.server.dto.AddressDTO;
import br.com.rodrigo.ecommerce.server.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressDTO dto);

    AddressDTO toDto(Address entity);
}