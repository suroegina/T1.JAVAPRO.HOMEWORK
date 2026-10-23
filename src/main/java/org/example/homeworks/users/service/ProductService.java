package org.example.homeworks.users.service;

import org.example.homeworks.users.dto.ProductDto;
import org.example.homeworks.users.product.Product;
import org.example.homeworks.users.product.ProductType;
import org.example.homeworks.users.user.User;
import org.example.homeworks.users.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
    }

    private ProductDto productDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getAccountNumber(),
                product.getBalance(),
                product.getProductType().name(),
                product.getUser().getId()
        );
    }

    public List<ProductDto> getProductsByUserId(Long userId) {
        return productRepository.findByUserId(userId).stream()
                .map(this::productDto)
                .collect(Collectors.toList());
    }

    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::productDto);
    }

    public List<ProductDto> getAllProductsDto() {
        return productRepository.findAll().stream()
                .map(this::productDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto createProduct(String accountNumber, BigDecimal balance, ProductType type, Long userId) {
        User user = userService.getUserById(userId);
        Product product = new Product(accountNumber, balance, type, user);
        Product saved = productRepository.save(product);
        return productDto(saved);
    }

    @Transactional
    public void deleteAll() {
        productRepository.deleteAllInBatch();
    }
}
