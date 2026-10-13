package br.com.rodrigo.ecommerce.server.repository;

import br.com.rodrigo.ecommerce.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

}