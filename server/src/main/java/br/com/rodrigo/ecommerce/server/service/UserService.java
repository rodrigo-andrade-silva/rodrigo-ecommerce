package br.com.rodrigo.ecommerce.server.service;

import br.com.rodrigo.ecommerce.server.model.User;
import br.com.rodrigo.ecommerce.server.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }
}