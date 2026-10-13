package br.com.rodrigo.ecommerce.server.service.impl;

import br.com.rodrigo.ecommerce.server.model.Category;
import br.com.rodrigo.ecommerce.server.repository.CategoryRepository;
import br.com.rodrigo.ecommerce.server.service.ICategoryService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends CrudServiceImpl<Category, Long>
        implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected JpaRepository<Category, Long> getRepository() {
        return categoryRepository;
    }
}