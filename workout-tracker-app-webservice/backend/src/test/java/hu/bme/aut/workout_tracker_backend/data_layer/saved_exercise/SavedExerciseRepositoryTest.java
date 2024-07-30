package hu.bme.aut.workout_tracker_backend.data_layer.saved_exercise;

import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import hu.bme.aut.workout_tracker_backend.data_layer.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class SavedExerciseRepositoryTest {

    @Autowired
    private SavedExerciseRepository savedExerciseRepository;

    @BeforeEach
    public void setUp() {
        SavedExercise savedExercise = new SavedExercise();
        savedExercise.setUserId("user1@example.com");

        savedExerciseRepository.save(savedExercise);
    }

    @Test
    public void testFindByEmail() {
        Optional<List<SavedExercise>> found = savedExerciseRepository.findByUserEmail("user1@example.com");
        assertThat(found).isPresent();
        assertThat(found.get()).hasSize(1);
    }

    @Test
    public void testFindByEmailNotFound() {
        Optional<List<SavedExercise>> found = savedExerciseRepository.findByUserEmail("nonexistent@example.com");
        assertThat(found).isPresent();
        assertThat(found.get()).hasSize(0);
    }
}
