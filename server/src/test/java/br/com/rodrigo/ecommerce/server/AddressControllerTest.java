package br.com.rodrigo.ecommerce.server;

import br.com.rodrigo.ecommerce.server.dto.AddressDTO;
import br.com.rodrigo.ecommerce.server.dto.AuthRequestDTO;
import br.com.rodrigo.ecommerce.server.model.Address;
import br.com.rodrigo.ecommerce.server.model.User;
import br.com.rodrigo.ecommerce.server.repository.AddressRepository;
import br.com.rodrigo.ecommerce.server.repository.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
public class AddressControllerTest {

    private static final String API_ADDRESSES = "/addresses";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void cleanup() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getAddresses_whenThereIsNoToken_receiveUnauthorized() {
        ResponseEntity<Object> response = testRestTemplate.getForEntity(API_ADDRESSES, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postAddress_whenTokenIsValid_receiveCreatedAndAddressBelongsToLoggedUser() {
        User user = userService.save(createUser("cliente-a"));
        String token = loginAndGetToken("cliente-a");

        ResponseEntity<Object> response = testRestTemplate.exchange(
                API_ADDRESSES, HttpMethod.POST, authorized(token, createValidAddressDTO()), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(addressRepository.count()).isEqualTo(1);
        Address saved = addressRepository.findAll().getFirst();
        assertThat(saved.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void postAddress_whenZipCodeIsInvalid_receiveBadRequest() {
        userService.save(createUser("cliente-a"));
        String token = loginAndGetToken("cliente-a");
        AddressDTO address = createValidAddressDTO();
        address.setZipCode("123");

        ResponseEntity<Object> response = testRestTemplate.exchange(
                API_ADDRESSES, HttpMethod.POST, authorized(token, address), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAddresses_whenTokenIsValid_receiveOnlyAddressesOfTheLoggedUser() {
        User owner = userService.save(createUser("cliente-a"));
        User other = userService.save(createUser("cliente-b"));
        addressRepository.save(createAddress(owner));
        addressRepository.save(createAddress(other));
        String token = loginAndGetToken("cliente-a");

        ResponseEntity<List> response = testRestTemplate.exchange(
                API_ADDRESSES, HttpMethod.GET, authorized(token, null), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    private User createUser(String username) {
        return User.builder()
                .username(username)
                .displayName("test-Display")
                .password("P4ssword")
                .build();
    }

    private AddressDTO createValidAddressDTO() {
        return AddressDTO.builder()
                .street("Rua das Flores")
                .number("100")
                .neighborhood("Centro")
                .city("Curitiba")
                .state("PR")
                .zipCode("80000000")
                .build();
    }

    private Address createAddress(User user) {
        return Address.builder()
                .user(user)
                .street("Rua das Flores")
                .number("100")
                .neighborhood("Centro")
                .city("Curitiba")
                .state("PR")
                .zipCode("80000000")
                .build();
    }

    private String loginAndGetToken(String username) {
        AuthRequestDTO credentials = new AuthRequestDTO();
        credentials.setUsername(username);
        credentials.setPassword("P4ssword");
        return testRestTemplate.postForEntity("/login", credentials, AuthenticationResponse.class)
                .getBody().getToken();
    }

    private <T> HttpEntity<T> authorized(String token, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(body, headers);
    }
}