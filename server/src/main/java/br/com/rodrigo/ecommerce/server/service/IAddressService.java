package br.com.rodrigo.ecommerce.server.service;

import br.com.rodrigo.ecommerce.server.model.Address;

import java.util.List;

public interface IAddressService {

    Address save(Address address, String username);

    List<Address> findByUsername(String username);
}
