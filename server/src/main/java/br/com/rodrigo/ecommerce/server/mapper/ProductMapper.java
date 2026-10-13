package br.com.rodrigo.ecommerce.server.mapper;

import br.com.rodrigo.ecommerce.server.dto.ProductDTO;
import br.com.rodrigo.ecommerce.server.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = CategoryMapper.class)
public interface ProductMapper {

    ProductDTO toDto(Product entity);

    Product toEntity(ProductDTO dto);
}