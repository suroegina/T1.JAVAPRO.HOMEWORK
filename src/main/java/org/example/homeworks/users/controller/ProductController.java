package org.example.homeworks.users.controller;

import org.example.homeworks.users.dto.ProductDto;
import org.example.homeworks.users.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//http://localhost:5438/suroegina/products/
//http://localhost:8080/suroegina/products/153
//http://localhost:8080/suroegina/products?userId=185

@RestController
@RequestMapping("/suroegina/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProductsByUserId(@RequestParam("userId") Long userId) {
        List<ProductDto> products = productService.getProductsByUserId(userId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProductsDto() {
        return ResponseEntity.ok(productService.getAllProductsDto());
    }


}
