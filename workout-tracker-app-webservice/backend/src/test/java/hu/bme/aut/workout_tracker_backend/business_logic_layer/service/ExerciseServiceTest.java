package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.api.ApiConstants;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    private Exercise exercise1;
    private Exercise exercise2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exercise1 = new Exercise();
        exercise1.setId(1L);
        exercise1.setName("Squat");

        exercise2 = new Exercise();
        exercise2.setId(2L);
        exercise2.setName("Push-up");
    }

    @Test
    void testCreateExercise() {
        exerciseService.createExercise(exercise1);
        verify(exerciseRepository, times(1)).save(exercise1);
    }

    @Test
    void testGetExercise_Success() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise1));

        Exercise result = exerciseService.getExercise(1L);

        assertThat(result).isEqualTo(exercise1);
        verify(exerciseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetExercise_NotFound() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            exerciseService.getExercise(1L);
        });

        String expectedMessage = "No such exercise";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
        verify(exerciseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise1);
        exercises.add(exercise2);
        when(exerciseRepository.findAll()).thenReturn(exercises);

        List<Exercise> result = exerciseService.getExercises();

        assertThat(result).hasSize(2);
        assertThat(result).contains(exercise1, exercise2);
        verify(exerciseRepository, times(1)).findAll();
    }

    @Test
    void testGetStandingsExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise1);
        exercises.add(exercise2);
        ApiConstants.standingExerciseList = Collections.singletonList("Squat");
        when(exerciseRepository.findAll()).thenReturn(exercises);

        List<Exercise> result = exerciseService.getStandingsExercises();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Squat");
        verify(exerciseRepository, times(1)).findAll();
    }
}
