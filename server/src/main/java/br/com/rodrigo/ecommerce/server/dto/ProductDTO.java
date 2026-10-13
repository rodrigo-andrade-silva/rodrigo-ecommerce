package br.com.rodrigo.ecommerce.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;

    @NotNull
    @Size(min = 2, max = 150)
    private String name;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal price;

    private String imageUrl;

    @NotNull
    @Size(min = 2, max = 100)
    private String author;

    @Size(max = 100)
    private String publisher;

    @NotNull
    @Size(min = 13, max = 13)
    private String isbn;

    private CategoryDTO category;
}