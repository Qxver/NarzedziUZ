package org.store.narzedziuz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add_product";
    }

    @PostMapping("/add-product")
    public String handleAddProduct(@ModelAttribute Product product, Model model) {
        System.out.println("Dodano produkt:");
        System.out.println("Nazwa: " + product.getName());
        System.out.println("Opis: " + product.getDescription());
        System.out.println("Cena: " + product.getPrice());
        System.out.println("URL zdjęcia: " + product.getImageUrl());

        model.addAttribute("product", product);
        return "redirect:/";
    }
}
