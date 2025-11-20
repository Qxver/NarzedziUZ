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
<<<<<<< Updated upstream
}
=======

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

>>>>>>> Stashed changes
