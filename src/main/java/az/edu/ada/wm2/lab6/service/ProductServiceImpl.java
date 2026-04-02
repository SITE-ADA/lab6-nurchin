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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product product = productMapper.toEntity(dto);
        attachCategories(product, dto.getCategoryIds());

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto getProductById(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toResponseDto)
                .orElse(null);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto updateProduct(UUID id, ProductRequestDto dto) {
        Product existing = productRepository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setProductName(dto.getProductName());
        existing.setPrice(dto.getPrice());
        existing.setExpirationDate(dto.getExpirationDate());
        attachCategories(existing, dto.getCategoryIds());

        return productMapper.toResponseDto(productRepository.save(existing));
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponseDto> getProductsExpiringBefore(LocalDate date) {
        return productRepository.findByExpirationDateBefore(date).stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }



        private void attachCategories(Product product, List<UUID> categoryIds) {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return;
            }

            List<Category> categories = categoryRepository.findAllById(categoryIds);
            product.setCategories(categories);

            for (Category category : categories) {
                if (!category.getProducts().contains(product)) {
                    category.getProducts().add(product);
                }
            }
        }


    }
}

