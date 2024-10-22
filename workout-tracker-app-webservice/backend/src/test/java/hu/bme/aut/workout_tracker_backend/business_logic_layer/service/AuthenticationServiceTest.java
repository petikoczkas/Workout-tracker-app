package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.api.ApiConstants;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication.AuthUserDetails;
import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import hu.bme.aut.workout_tracker_backend.data_layer.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRoles("ROLE_USER");
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(repository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        AuthUserDetails userDetails = (AuthUserDetails) authenticationService.loadUserByUsername("user@example.com");

        assertThat(userDetails.getUsername()).isEqualTo("user@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("password");
        verify(repository, times(1)).findByEmail("user@example.com");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(repository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.loadUserByUsername("user@example.com");
        });

        String expectedMessage = ApiConstants.userNotFoundWithEmailMessage + "user@example.com";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
        verify(repository, times(1)).findByEmail("user@example.com");
    }

    @Test
    void testAddUser_Success() {
        when(repository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(encoder.encode("password")).thenReturn("encodedPassword");

        String result = authenticationService.addUser(user);

        assertThat(result).isEqualTo(ApiConstants.userAddedMessage);
        verify(repository, times(1)).findByEmail("user@example.com");
        verify(repository, times(1)).save(user);
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        assertThat(user.getPhoto()).isEqualTo(new byte[0]);
    }

    @Test
    void testAddUser_UserAlreadyExists() {
        when(repository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            authenticationService.addUser(user);
        });

        verify(repository, times(1)).findByEmail("user@example.com");
        verify(repository, times(0)).save(any(User.class));
    }
}
