package br.com.rodrigo.ecommerce.server.controller;

import br.com.rodrigo.ecommerce.server.model.User;
import br.com.rodrigo.ecommerce.server.service.UserService;
import br.com.rodrigo.ecommerce.server.shared.GenericResponse;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GenericResponse> createUser(@RequestBody User user) {
        this.userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse("Usuário salvo com sucesso"));
    }
}