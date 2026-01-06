package org.store.narzedziuz.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

@Data
public class ProductFormDto {
    private String name;
    private BigDecimal price;
    private String description;
    private Integer quantity;
    private Long categoryId;
    private String manufacturer;
    private MultipartFile image;
    private String imageFilename;  // For storing the saved filename
}