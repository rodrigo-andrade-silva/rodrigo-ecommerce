package br.com.rodrigo.ecommerce.server.controller;

import br.com.rodrigo.ecommerce.server.dto.ProductDTO;
import br.com.rodrigo.ecommerce.server.mapper.ProductMapper;
import br.com.rodrigo.ecommerce.server.model.Product;
import br.com.rodrigo.ecommerce.server.service.ICrudService;
import br.com.rodrigo.ecommerce.server.service.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
public class ProductController extends CrudController<Product, ProductDTO, Long> {

    private final IProductService productService;
    private final ProductMapper productMapper;

    public ProductController(IProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    protected ICrudService<Product, Long> getService() {
        return productService;
    }

    @Override
    protected ProductDTO toDto(Product entity) {
        return productMapper.toDto(entity);
    }

    @Override
    protected Product toEntity(ProductDTO dto) {
        return productMapper.toEntity(dto);
    }

    // http://localhost:8080/products/category/1?page=0&size=5
    @GetMapping("category/{categoryId}")
    public ResponseEntity<Page<ProductDTO>> findByCategory(@PathVariable Long categoryId,
                                                           @RequestParam int page,
                                                           @RequestParam int size,
                                                           @RequestParam(required = false) String order,
                                                           @RequestParam(required = false) Boolean asc) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (order != null && asc != null) {
            pageRequest = PageRequest.of(page, size,
                    asc ? Sort.Direction.ASC : Sort.Direction.DESC, order);
        }
        return ResponseEntity.ok(
                productService.findByCategoryId(categoryId, pageRequest).map(productMapper::toDto));
    }
}