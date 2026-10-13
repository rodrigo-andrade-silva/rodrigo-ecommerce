package br.com.rodrigo.ecommerce.server.repository;

import br.com.rodrigo.ecommerce.server.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // select * from tb_product where category_id = ? (com paginação)
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

}