package br.com.rodrigo.ecommerce.server;

import br.com.rodrigo.ecommerce.server.dto.AuthRequestDTO;
import br.com.rodrigo.ecommerce.server.model.User;
import br.com.rodrigo.ecommerce.server.repository.UserRepository;
import br.com.rodrigo.ecommerce.server.security.dto.AuthenticationResponse;
import br.com.rodrigo.ecommerce.server.security.dto.UserResponseDTO;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
public class AuthControllerTest {

    private static final String API_LOGIN = "/login";
    private static final String API_USER_INFO = "/auth/user-info";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void postLogin_whenCredentialsAreValid_receiveOKWithToken() {
        userService.save(createValidUser());

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                API_LOGIN, createCredentials("test-user", "P4ssword"), AuthenticationResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getToken()).isNotNull();
    }

    @Test
    public void postLogin_whenPasswordIsWrong_receiveUnauthorized() {
        userService.save(createValidUser());

        ResponseEntity<Object> response = testRestTemplate.postForEntity(
                API_LOGIN, createCredentials("test-user", "WrongP4ss"), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postLogin_whenUserDoesNotExist_receiveUnauthorized() {
        ResponseEntity<Object> response = testRestTemplate.postForEntity(
                API_LOGIN, createCredentials("nobody", "P4ssword"), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getUserInfo_whenThereIsNoToken_receiveUnauthorized() {
        ResponseEntity<Object> response = testRestTemplate.getForEntity(API_USER_INFO, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getUserInfo_whenTokenIsInvalid_receiveUnauthorized() {
        ResponseEntity<Object> response = testRestTemplate.exchange(
                API_USER_INFO, HttpMethod.GET, authorized("token-invalido"), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getUserInfo_whenTokenIsValid_receiveOK() {
        userService.save(createValidUser());
        String token = loginAndGetToken("test-user", "P4ssword");

        ResponseEntity<UserResponseDTO> response = testRestTemplate.exchange(
                API_USER_INFO, HttpMethod.GET, authorized(token), UserResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUsername()).isEqualTo("test-user");
    }

    private User createValidUser() {
        return User.builder()
                .username("test-user")
                .displayName("test-Display")
                .password("P4ssword")
                .build();
    }

    private AuthRequestDTO createCredentials(String username, String password) {
        AuthRequestDTO credentials = new AuthRequestDTO();
        credentials.setUsername(username);
        credentials.setPassword(password);
        return credentials;
    }

    private String loginAndGetToken(String username, String password) {
        return testRestTemplate.postForEntity(API_LOGIN, createCredentials(username, password),
                AuthenticationResponse.class).getBody().getToken();
    }

    private HttpEntity<Void> authorized(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}