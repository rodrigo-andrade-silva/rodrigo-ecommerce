package br.com.rodrigo.ecommerce.server.controller;

import br.com.rodrigo.ecommerce.server.model.User;
import br.com.rodrigo.ecommerce.server.security.dto.UserResponseDTO;
import br.com.rodrigo.ecommerce.server.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("user-info")
    public UserResponseDTO getUserInfo(Principal principal) {
        // O Principal traz o username do usuário autenticado pelo token
        String username = principal.getName();
        User user = (User) authService.loadUserByUsername(username);
        return new UserResponseDTO(user);
    }
}