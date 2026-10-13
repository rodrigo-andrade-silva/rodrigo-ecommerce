package br.com.rodrigo.ecommerce.server.mapper;

import br.com.rodrigo.ecommerce.server.dto.CategoryDTO;
import br.com.rodrigo.ecommerce.server.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryDTO toDto(Category entity);

    Category toEntity(CategoryDTO dto);
}