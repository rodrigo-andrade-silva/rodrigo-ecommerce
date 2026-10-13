package br.com.rodrigo.ecommerce.server.repository;

import br.com.rodrigo.ecommerce.server.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}