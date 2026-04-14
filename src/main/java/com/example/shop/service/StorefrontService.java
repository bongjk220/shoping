package com.example.shop.service;

import com.example.shop.entity.Product;
import com.example.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorefrontService {
    private final ProductRepository productRepository;

    public StorefrontService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findAll().stream()
                .filter(Product::isFeatured)
                .limit(4)
                .toList();
    }

    public List<Product> getBestProducts() {
        return productRepository.findTop6ByOrderBySalesCountDesc();
    }
}
