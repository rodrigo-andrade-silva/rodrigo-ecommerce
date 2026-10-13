package br.com.rodrigo.ecommerce.server;

import br.com.rodrigo.ecommerce.server.model.User;
import br.com.rodrigo.ecommerce.server.repository.UserRepository;
import br.com.rodrigo.ecommerce.server.shared.GenericResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List; // <-- Import adicionado na etapa 5.4

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
public class UserControllerTest {

    private static final String API_USER = "/users";

    @Autowired
    private TestRestTemplate testRestTemplate;

    // Declarando userRepo e limpeza antes de teste
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    // 5.1
    @Test
    public void postUser_whenUserIsValid_receiveCREATED() {
        User user = createValidUser();

        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_USER, user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    // 5.2
    @Test
    public void postUser_whenUserIsValid_userSavedToDatabase() {
        User user = createValidUser();

        testRestTemplate.postForEntity(API_USER, user, Object.class);

        assertThat(userRepository.count()).isEqualTo(1);
    }

    // 5.3
    @Test
    public void postUser_whenUserIsValid_receiveSuccessMessage() {
        User user = createValidUser();

        ResponseEntity<GenericResponse> response =
                testRestTemplate.postForEntity(API_USER, user, GenericResponse.class);

        assertThat(response.getBody().getMessage()).isNotNull();
    }

    // 5.4 - Teste de criptografia da senha
    @Test
    public void postUser_whenUserIsValid_passwordIsHashedInDatabase() {
        User user = createValidUser();

        testRestTemplate.postForEntity(API_USER, user, Object.class);

        List<User> users = userRepository.findAll();
        User userDB = users.getFirst();

        assertThat(user.getPassword()).isNotEqualTo(userDB.getPassword());
    }

    private User createValidUser() {
        return User.builder()
                .username("test-user")
                .displayName("test-Display")
                .password("P4ssword")
                .build();
    }
}