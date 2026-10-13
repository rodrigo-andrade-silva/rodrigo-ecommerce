package br.com.rodrigo.ecommerce.server.repository;

import br.com.rodrigo.ecommerce.server.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // select * from tb_address where user_id = ? order by id desc
    List<Address> findByUserIdOrderByIdDesc(Long userId);

}