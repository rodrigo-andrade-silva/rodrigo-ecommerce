package br.com.rodrigo.ecommerce.server.controller;

import br.com.rodrigo.ecommerce.server.dto.UserDTO;
import br.com.rodrigo.ecommerce.server.mapper.UserMapper;
import br.com.rodrigo.ecommerce.server.service.UserService;
import br.com.rodrigo.ecommerce.server.shared.GenericResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GenericResponse> createUser(@RequestBody @Valid UserDTO userDTO) {
        this.userService.save(userMapper.toEntity(userDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse("Usuário salvo com sucesso"));
    }

}