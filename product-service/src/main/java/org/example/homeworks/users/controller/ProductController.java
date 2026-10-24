package org.example.homeworks.users.controller;

import org.example.homeworks.users.dto.DebitRequest;
import org.example.homeworks.users.dto.ProductDto;
import org.example.homeworks.users.exception.ProductException;
import org.example.homeworks.users.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//http://localhost:8080/v1/suroegina/products/
//http://localhost:8080/v1/suroegina/products/153
//http://localhost:8080/v1/suroegina/products?userId=185

@RestController
@RequestMapping("/v1/suroegina/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new ProductException(
                        HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "Product not found with id: " + id));
    }

    @GetMapping
    public List<ProductDto> getProductsByUserId(@RequestParam("userId") Long userId) {
        List<ProductDto> products = productService.getProductsByUserId(userId);
        return products;
    }

    @GetMapping("/")
    public List<ProductDto> getAllProductsDto() {
        return productService.getAllProductsDto();
    }

    /**
     * Списание средств с продукта. Вызывается платёжным ядром через RestTemplate.
     */
    @PostMapping("/{id}/debit")
    public ProductDto debit(@PathVariable Long id, @RequestBody DebitRequest request) {
        return productService.debit(id, request.amount());
    }


}
