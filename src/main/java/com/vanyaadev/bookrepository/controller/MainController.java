package com.vanyaadev.bookrepository.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {
    @GetMapping("/home")
    String homePage() {
        return "home";
    }

    @GetMapping("/")
    RedirectView homeLoginPage() {
        return new RedirectView("/login");
    }
}
