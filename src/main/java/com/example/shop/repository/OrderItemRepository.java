package com.example.shop.repository;

import com.example.shop.entity.OrderItem;
import com.example.shop.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    List<OrderItem> findByOrderOrderByProductNameAsc(PurchaseOrder order);
}
