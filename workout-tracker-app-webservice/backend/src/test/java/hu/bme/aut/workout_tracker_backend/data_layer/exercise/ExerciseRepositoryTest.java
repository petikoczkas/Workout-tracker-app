package hu.bme.aut.workout_tracker_backend.data_layer.exercise;

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
public class ExerciseRepositoryTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    private Exercise exercise1;
    private Exercise exercise2;

    @BeforeEach
    public void setUp() {
        exercise1 = new Exercise();
        exercise1.setName("Push-up");

        exercise2 = new Exercise();
        exercise2.setName("Squat");

        exerciseRepository.save(exercise1);
        exerciseRepository.save(exercise2);
    }

    @Test
    public void testFindById() {
        Optional<Exercise> found = exerciseRepository.findById(exercise1.getId());
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(exercise1);
    }

    @Test
    public void testFindAll() {
        Iterable<Exercise> exercises = exerciseRepository.findAll();
        assertThat(exercises).hasSize(2).contains(exercise1, exercise2);
    }

    @Test
    public void testDeleteById() {
        exerciseRepository.deleteById(exercise1.getId());
        Optional<Exercise> found = exerciseRepository.findById(exercise1.getId());
        assertThat(found).isNotPresent();
    }
}
