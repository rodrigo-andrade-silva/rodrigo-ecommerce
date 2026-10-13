package br.com.rodrigo.ecommerce.server.service.impl;

import br.com.rodrigo.ecommerce.server.model.Product;
import br.com.rodrigo.ecommerce.server.repository.ProductRepository;
import br.com.rodrigo.ecommerce.server.service.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl extends CrudServiceImpl<Product, Long>
        implements IProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected JpaRepository<Product, Long> getRepository() {
        return productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

}