package com.example.shop.service;

import com.example.shop.entity.Category;
import com.example.shop.entity.OrderItem;
import com.example.shop.entity.Product;
import com.example.shop.entity.PurchaseOrder;
import com.example.shop.entity.User;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.PurchaseOrderRepository;
import com.example.shop.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class SeedDataService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    public SeedDataService(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    @Transactional
    public void seed() {
        if (categoryRepository.count() == 0) {
            seedCatalog();
        }

        User admin = userRepository.findByUsername("admin").orElseGet(() -> {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("admin1234");
            user.setEmail("admin@dadrim-mall.com");
            user.setFullName("관리자 김다림");
            user.setRole("ADMIN");
            return userRepository.save(user);
        });

        User customer = userRepository.findByUsername("hong").orElseGet(() -> {
            User user = new User();
            user.setUsername("hong");
            user.setPassword("1234");
            user.setEmail("hong@example.com");
            user.setFullName("홍길동");
            user.setRole("CUSTOMER");
            return userRepository.save(user);
        });

        if (purchaseOrderRepository.count() == 0) {
            Product avocado = productRepository.findTop6ByOrderBySalesCountDesc().stream()
                    .filter(product -> product.getName().contains("아보카도"))
                    .findFirst()
                    .orElseThrow();
            Product beef = productRepository.findTop6ByOrderBySalesCountDesc().stream()
                    .filter(product -> product.getName().contains("스테이크"))
                    .findFirst()
                    .orElseThrow();
            Product milk = productRepository.findTop6ByOrderBySalesCountDesc().stream()
                    .filter(product -> product.getName().contains("우유"))
                    .findFirst()
                    .orElseThrow();
            Product salad = productRepository.findTop6ByOrderBySalesCountDesc().stream()
                    .filter(product -> product.getName().contains("샐러드"))
                    .findFirst()
                    .orElseThrow();
            Product gift = productRepository.findTop6ByOrderBySalesCountDesc().stream()
                    .filter(product -> product.getName().contains("선물 박스"))
                    .findFirst()
                    .orElseThrow();

            PurchaseOrder order1 = order(customer, "ORD-2026-8842", "PROCESSING", "45600", 3, "경기도 성남시 분당구 판교로 188");
            PurchaseOrder order2 = order(customer, "ORD-2026-8841", "QUEUED", "12800", 1, "서울시 송파구 올림픽로 35");
            PurchaseOrder order3 = order(customer, "ORD-2026-8840", "FAILED", "89000", 2, "부산시 해운대구 센텀중앙로 97");

            orderItem(order1, avocado, 1, "12800");
            orderItem(order1, salad, 1, "6900");
            orderItem(order1, milk, 1, "5200");
            orderItem(order2, avocado, 1, "12800");
            orderItem(order3, gift, 1, "45000");
            orderItem(order3, beef, 1, "44000");
        }
    }

    private void seedCatalog() {
        Category fresh = category("신선식품", "매일 입고되는 과일과 채소");
        Category meal = category("간편식", "빠르게 조리 가능한 인기 상품");
        Category premium = category("프리미엄", "선물용과 고급 식재료");

        product(fresh, "프리미엄 아보카도 세트", "숙성 상태가 고른 2입 패키지", "SEASON HIT", "🥑", "12800", 28, 188, true);
        product(premium, "한우 채끝 스테이크", "홈파티용 1등급 채끝 300g", "PREMIUM", "🥩", "28900", 16, 94, true);
        product(meal, "목장우유 1L", "아침 배송으로 받는 신선한 우유", "DAILY", "🥛", "5200", 65, 141, false);
        product(meal, "그린 샐러드 믹스", "씻어서 바로 먹는 샐러드 채소", "READY", "🥗", "6900", 55, 126, false);
        product(premium, "제철 과일 선물 박스", "명절과 기념일에 잘 어울리는 프리미엄 구성", "GIFT", "🎁", "45000", 10, 61, true);
        product(fresh, "유기농 대추방울 토마토", "샐러드와 도시락에 잘 어울리는 달콤한 토마토", "ORGANIC", "🍅", "7900", 42, 312, true);
    }

    private Category category(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return categoryRepository.save(category);
    }

    private Product product(Category category, String name, String summary, String badge, String emoji,
                            String price, int stock, int salesCount, boolean featured) {
        Product product = new Product();
        product.setCategory(category);
        product.setName(name);
        product.setSummary(summary);
        product.setBadge(badge);
        product.setEmoji(emoji);
        product.setImageTheme(name);
        product.setPrice(new BigDecimal(price));
        product.setStockQuantity(stock);
        product.setSalesCount(salesCount);
        product.setFeatured(featured);
        return productRepository.save(product);
    }

    private PurchaseOrder order(User user, String orderNumber, String status, String amount, int itemCount, String address) {
        PurchaseOrder order = new PurchaseOrder();
        order.setUser(user);
        order.setOrderNumber(orderNumber);
        order.setStatus(status);
        order.setTotalAmount(new BigDecimal(amount));
        order.setItemCount(itemCount);
        order.setShippingAddress(address);
        return purchaseOrderRepository.save(order);
    }

    private void orderItem(PurchaseOrder order, Product product, int quantity, String unitPrice) {
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(new BigDecimal(unitPrice));
        orderItemRepository.save(item);
    }
}
