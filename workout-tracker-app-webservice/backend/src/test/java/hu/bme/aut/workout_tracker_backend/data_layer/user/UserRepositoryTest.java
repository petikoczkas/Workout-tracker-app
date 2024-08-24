package hu.bme.aut.workout_tracker_backend.data_layer.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;

    @BeforeEach
    public void setUp() {
        user1 = new User();
        user1.setEmail("user1@example.com");

        userRepository.save(user1);
    }

    @Test
    public void testFindByEmail() {
        Optional<User> found = userRepository.findByEmail("user1@example.com");
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(user1);
    }

    @Test
    public void testFindByEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");
        assertThat(found).isNotPresent();
    }
}
