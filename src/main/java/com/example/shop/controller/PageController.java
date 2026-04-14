package com.example.shop.controller;

import com.example.shop.service.AdminDashboardService;
import com.example.shop.service.StorefrontService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    private final StorefrontService storefrontService;
    private final AdminDashboardService adminDashboardService;

    public PageController(StorefrontService storefrontService, AdminDashboardService adminDashboardService) {
        this.storefrontService = storefrontService;
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/")
    public String landing() {
        return "redirect:/store";
    }

    @GetMapping("/store")
    public String store(Model model) {
        model.addAttribute("featuredProducts", storefrontService.getFeaturedProducts());
        model.addAttribute("bestProducts", storefrontService.getBestProducts());
        return "store";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("summary", adminDashboardService.getSummary());
        model.addAttribute("products", adminDashboardService.getProducts());
        model.addAttribute("orders", adminDashboardService.getRecentOrders());
        model.addAttribute("members", adminDashboardService.getMembers());
        return "admin-dashboard";
    }
}
