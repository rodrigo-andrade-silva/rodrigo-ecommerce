package br.com.rodrigo.ecommerce.server.controller;

import br.com.rodrigo.ecommerce.server.dto.AddressDTO;
import br.com.rodrigo.ecommerce.server.mapper.AddressMapper;
import br.com.rodrigo.ecommerce.server.model.Address;
import br.com.rodrigo.ecommerce.server.service.IAddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("addresses")
public class AddressController {

    private final IAddressService addressService;
    private final AddressMapper addressMapper;

    public AddressController(IAddressService addressService, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    @PostMapping
    public ResponseEntity<AddressDTO> create(@RequestBody @Valid AddressDTO addressDTO, Principal principal) {
        Address address = addressService.save(addressMapper.toEntity(addressDTO), principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(addressMapper.toDto(address));
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> findAll(Principal principal) {
        return ResponseEntity.ok(
                addressService.findByUsername(principal.getName())
                        .stream()
                        .map(addressMapper::toDto)
                        .collect(Collectors.toList())
        );
    }
}