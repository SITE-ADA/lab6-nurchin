package az.edu.ada.wm2.lab6.service;

import az.edu.ada.wm2.lab6.model.Category;
import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.CategoryRequestDto;
import az.edu.ada.wm2.lab6.model.dto.CategoryResponseDto;
import az.edu.ada.wm2.lab6.model.dto.ProductResponseDto;
import az.edu.ada.wm2.lab6.model.mapper.CategoryMapper;
import az.edu.ada.wm2.lab6.model.mapper.ProductMapper;
import az.edu.ada.wm2.lab6.repository.CategoryRepository;
import az.edu.ada.wm2.lab6.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public CategoryResponseDto create(CategoryRequestDto dto) {
        Category category = CategoryMapper.toEntity(dto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toResponseDto(savedCategory);
    }

    @Override
    public List<CategoryResponseDto> getAll() {
        return categoryRepository.findAll().stream().map(CategoryMapper::toResponseDto).toList();
    }

    @Override
    public CategoryResponseDto addProduct(UUID categoryId, UUID productId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if (category != null && product != null) {
            product.getCategories().add(category);
            productRepository.save(product);
        }
        return CategoryMapper.toResponseDto(category);
    }

    @Override
    public List<ProductResponseDto> getProducts(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null || category.getProducts() == null) return List.of();

        return category.getProducts().stream().map(productMapper::toResponseDto).toList();
    }
}
