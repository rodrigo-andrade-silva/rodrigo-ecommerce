package br.com.rodrigo.ecommerce.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "tb_product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // título do livro
    @NotNull
    @Size(min = 2, max = 150)
    @Column(length = 150)
    private String name;

    @NotNull
    @Column(length = 1024)
    private String description;

    @NotNull
    private BigDecimal price;

    // capa do livro (URL externa)
    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(length = 100)
    private String author;

    @Size(max = 100)
    @Column(length = 100)
    private String publisher;

    // ISBN-13, somente números
    @NotNull
    @Size(min = 13, max = 13)
    @Column(length = 13, unique = true)
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}