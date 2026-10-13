package br.com.rodrigo.ecommerce.server;

import br.com.rodrigo.ecommerce.server.model.User;
import br.com.rodrigo.ecommerce.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
public class UserControllerTest {

    private static final String API_USER = "/users";

    @Autowired
    private TestRestTemplate testRestTemplate;

    //Declarando userRepo e limpeza antes de teste
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void postUser_whenUserIsValid_receiveCREATED() {
        User user = createValidUser();

        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_USER, user, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    //Segundo teste
    @Test
    public void postUser_whenUserIsValid_userSavedToDatabase() {
        User user = createValidUser();

        testRestTemplate.postForEntity(API_USER, user, Object.class);

        assertThat(userRepository.count()).isEqualTo(1);
    }

    private User createValidUser() {
        return User.builder()
                .username("test-user")
                .displayName("test-Display")
                .password("P4ssword")
                .build();
    }
}