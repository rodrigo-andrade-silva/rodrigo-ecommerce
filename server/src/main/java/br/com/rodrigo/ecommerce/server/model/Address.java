package br.com.rodrigo.ecommerce.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "tb_address")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(length = 100)
    private String street;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(length = 10)
    private String number;

    @Size(max = 100)
    @Column(length = 100)
    private String complement;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(length = 60)
    private String neighborhood;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(length = 60)
    private String city;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(length = 2)
    private String state;

    @NotNull
    @Pattern(regexp = "^\\d{8}$", message = "{address.zipCode.pattern}")
    @Column(name = "zip_code", length = 8)
    private String zipCode;
}