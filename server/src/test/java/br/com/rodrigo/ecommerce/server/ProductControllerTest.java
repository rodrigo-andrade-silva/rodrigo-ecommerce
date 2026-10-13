package br.com.rodrigo.ecommerce.server;

import br.com.rodrigo.ecommerce.server.dto.AuthRequestDTO;
import br.com.rodrigo.ecommerce.server.model.User;
import br.com.rodrigo.ecommerce.server.model.Category;
import br.com.rodrigo.ecommerce.server.repository.UserRepository;
import br.com.rodrigo.ecommerce.server.repository.CategoryRepository;
import br.com.rodrigo.ecommerce.server.security.dto.AuthenticationResponse;
import br.com.rodrigo.ecommerce.server.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
public class ProductControllerTest {

    private static final String API_PRODUCTS = "/products";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void getProducts_whenThereIsNoToken_receiveOK() {
        ResponseEntity<Object> response = testRestTemplate.getForEntity(API_PRODUCTS, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getProducts_whenTokenIsInvalid_receiveOK() {
        ResponseEntity<Object> response = testRestTemplate.exchange(
                API_PRODUCTS, HttpMethod.GET, authorized("token-invalido"), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getProductsPage_whenSizeIsFive_receiveFiveProducts() {
        ResponseEntity<Map> response = testRestTemplate.getForEntity(
                API_PRODUCTS + "/page?page=0&size=5", Map.class);

        List<?> content = (List<?>) response.getBody().get("content");
        assertThat(content).hasSize(5);
    }

    @Test
    public void getProductsByCategory_whenCategoryExists_receiveOnlyProductsOfThatCategory() {
        Category category = categoryRepository.findAll().getFirst();

        ResponseEntity<Map> response = testRestTemplate.getForEntity(
                API_PRODUCTS + "/category/" + category.getId() + "?page=0&size=10", Map.class);

        List<?> content = (List<?>) response.getBody().get("content");
        assertThat(content).isNotEmpty();
        for (Object item : content) {
            Map<?, ?> product = (Map<?, ?>) item;
            Map<?, ?> productCategory = (Map<?, ?>) product.get("category");
            assertThat(productCategory.get("id").toString()).isEqualTo(category.getId().toString());
        }
    }

    @Test
    public void getProductById_whenProductDoesNotExist_receiveNoContent() {
        ResponseEntity<Object> response = testRestTemplate.getForEntity(API_PRODUCTS + "/9999", Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void deleteProduct_whenThereIsNoToken_receiveUnauthorized() {
        ResponseEntity<Object> response = testRestTemplate.exchange(
                API_PRODUCTS + "/1", HttpMethod.DELETE, null, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void deleteProduct_whenTokenIsValid_receiveForbidden() {
        userService.save(createValidUser());
        String token = loginAndGetToken();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                API_PRODUCTS + "/1", HttpMethod.DELETE, authorized(token), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private User createValidUser() {
        return User.builder()
                .username("test-user")
                .displayName("test-Display")
                .password("P4ssword")
                .build();
    }

    private String loginAndGetToken() {
        AuthRequestDTO credentials = new AuthRequestDTO();
        credentials.setUsername("test-user");
        credentials.setPassword("P4ssword");
        return testRestTemplate.postForEntity("/login", credentials, AuthenticationResponse.class)
                .getBody().getToken();
    }

    private HttpEntity<Void> authorized(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}