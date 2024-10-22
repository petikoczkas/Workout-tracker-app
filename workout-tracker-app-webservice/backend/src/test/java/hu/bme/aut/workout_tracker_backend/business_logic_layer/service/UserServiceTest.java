package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.UserDTO;
import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import hu.bme.aut.workout_tracker_backend.data_layer.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoto(new byte[]{1, 2, 3});

        userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setPhoto(new byte[]{1, 2, 3});
    }

    @Test
    void testGetUser() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDTO result = userService.getUser("user@example.com");

        assertThat(result.getEmail()).isEqualTo("user@example.com");
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getPhoto()).isEqualTo(new byte[]{1, 2, 3});
    }

    @Test
    void testGetUserThrowsExceptionWhenNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUser("user@example.com"));
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        userService.updateUser(userDTO);

        verify(userRepository).save(user);
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPhoto()).isEqualTo(new byte[]{1, 2, 3});
    }

    @Test
    void testUpdateUserThrowsExceptionWhenNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(userDTO));
    }

    @Test
    void testGetUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        List<UserDTO> result = userService.getUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("user@example.com");
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(0).getLastName()).isEqualTo("Doe");
        assertThat(result.get(0).getPhoto()).isEqualTo(new byte[]{1, 2, 3});
    }
}
