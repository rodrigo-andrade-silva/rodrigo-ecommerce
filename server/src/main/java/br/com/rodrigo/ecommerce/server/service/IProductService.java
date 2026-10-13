package br.com.rodrigo.ecommerce.server.service;

import br.com.rodrigo.ecommerce.server.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService extends ICrudService<Product, Long> {

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}