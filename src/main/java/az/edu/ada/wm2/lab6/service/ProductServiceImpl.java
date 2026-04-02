package az.edu.ada.wm2.lab6.service;

import az.edu.ada.wm2.lab6.model.Category;
import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.ProductRequestDto;
import az.edu.ada.wm2.lab6.model.dto.ProductResponseDto;
import az.edu.ada.wm2.lab6.model.mapper.ProductMapper;
import az.edu.ada.wm2.lab6.repository.CategoryRepository;
import az.edu.ada.wm2.lab6.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product product = productMapper.toEntity(dto);
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
            product.setCategories(categories);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto getProductById(UUID id) {
        return productRepository.findById(id).map(productMapper::toResponseDto).orElse(null);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toResponseDto).toList();
    }

    @Override
    public ProductResponseDto updateProduct(UUID id, ProductRequestDto dto) {
        Product existing = productRepository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setProductName(dto.getProductName());
        existing.setPrice(dto.getPrice());
        existing.setExpirationDate(dto.getExpirationDate());

        if (dto.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
            existing.setCategories(categories);
        }

        return productMapper.toResponseDto(productRepository.save(existing));
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponseDto> getProductsExpiringBefore(LocalDate date) {
        return productRepository.findByExpirationDateBefore(date).stream().map(productMapper::toResponseDto).toList();
    }

    @Override
    public List<ProductResponseDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream().map(productMapper::toResponseDto).toList();
    }
}
