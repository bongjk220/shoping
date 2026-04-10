package com.example.shop.controller;

import com.example.shop.entity.User;
import com.example.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/login"; // 회원가입 후 로그인 화면으로 이동
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            model.addAttribute("user", user);
            return "home"; // 로그인 성공 시 home.html로 이동
        }
        return "login"; // 실패 시 다시 로그인 화면
    }
}