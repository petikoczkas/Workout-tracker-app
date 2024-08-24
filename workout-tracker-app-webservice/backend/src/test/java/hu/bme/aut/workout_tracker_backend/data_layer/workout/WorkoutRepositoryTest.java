package hu.bme.aut.workout_tracker_backend.data_layer.workout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class WorkoutRepositoryTest {

    @Autowired
    private WorkoutRepository workoutRepository;

    private Workout workout1;
    private Workout workout2;

    @BeforeEach
    public void setUp() {
        workout1 = new Workout();
        workout1.setName("Test User1");
        workout1.setUserId("user@example.com");
        workout1.setIsFavorite(true);
        workout1.setExercises(Arrays.asList(1L, 2L));

        workout2 = new Workout();
        workout2.setName("Test User2");
        workout2.setUserId("user@example.com");
        workout2.setIsFavorite(false);
        workout2.setExercises(Arrays.asList(3L, 4L));

        workoutRepository.save(workout1);
        workoutRepository.save(workout2);
    }

    @Test
    public void testFindByUserEmail() {
        Optional<List<Workout>> found = workoutRepository.findByUserEmail("user@example.com");
        assertThat(found).isPresent();
        assertThat(found.get()).hasSize(2);
    }

    @Test
    public void testFindByUserEmailNotFound() {
        Optional<List<Workout>> found = workoutRepository.findByUserEmail("nonexistent@example.com");
        assertThat(found).isPresent();
        assertThat(found.get()).hasSize(0);
    }

    @Test
    public void testFindByUserEmailAndIsFavorite() {
        Optional<List<Workout>> found = workoutRepository.findByUserEmailAndIsFavorite("user@example.com");
        assertThat(found).isPresent();
        assertThat(found.get()).hasSize(1);
        assertThat(found.get().get(0)).isEqualTo(workout1);
    }
}
