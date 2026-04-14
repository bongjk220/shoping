package com.example.shop.service;

import com.example.shop.entity.Product;
import com.example.shop.entity.PurchaseOrder;
import com.example.shop.entity.User;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.PurchaseOrderRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class AdminDashboardService {
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;

    public AdminDashboardService(
            ProductRepository productRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            UserRepository userRepository
    ) {
        this.productRepository = productRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.userRepository = userRepository;
    }

    public DashboardSummary getSummary() {
        List<Product> products = productRepository.findAll();
        List<PurchaseOrder> orders = purchaseOrderRepository.findTop8ByOrderByOrderedAtDesc();
        List<User> members = userRepository.findAll();

        int totalSalesCount = products.stream().mapToInt(Product::getSalesCount).sum();
        int totalStock = products.stream().mapToInt(Product::getStockQuantity).sum();
        BigDecimal revenue = purchaseOrderRepository.findAll().stream()
                .map(PurchaseOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardSummary(
                products.size(),
                members.size(),
                totalSalesCount,
                totalStock,
                formatCurrency(revenue),
                orders
        );
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<PurchaseOrder> getRecentOrders() {
        return purchaseOrderRepository.findTop8ByOrderByOrderedAtDesc();
    }

    public List<User> getMembers() {
        return userRepository.findAll();
    }

    private String formatCurrency(Number amount) {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(amount);
    }

    public record DashboardSummary(
            int totalProducts,
            int totalMembers,
            int totalSalesCount,
            int totalStock,
            String totalRevenue,
            List<PurchaseOrder> recentOrders
    ) {
    }
}
