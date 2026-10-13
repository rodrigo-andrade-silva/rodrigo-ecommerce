package br.com.rodrigo.ecommerce.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String street;

    @NotNull
    @Size(min = 1, max = 10)
    private String number;

    @Size(max = 100)
    private String complement;

    @NotNull
    @Size(min = 2, max = 60)
    private String neighborhood;

    @NotNull
    @Size(min = 2, max = 60)
    private String city;

    @NotNull
    @Size(min = 2, max = 2)
    private String state;

    @NotNull
    @Pattern(regexp = "^\\d{8}$", message = "{address.zipCode.pattern}")
    private String zipCode;
}