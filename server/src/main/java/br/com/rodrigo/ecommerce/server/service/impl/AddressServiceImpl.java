package br.com.rodrigo.ecommerce.server.service.impl;

import br.com.rodrigo.ecommerce.server.model.Address;
import br.com.rodrigo.ecommerce.server.model.User;
import br.com.rodrigo.ecommerce.server.repository.AddressRepository;
import br.com.rodrigo.ecommerce.server.repository.UserRepository;
import br.com.rodrigo.ecommerce.server.service.IAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Address save(Address address, String username) {
        User user = userRepository.findUserByUsername(username);
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        return addressRepository.findByUserIdOrderByIdDesc(user.getId());
    }
}