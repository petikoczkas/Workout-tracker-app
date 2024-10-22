package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.WorkoutDTO;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.workout.Workout;
import hu.bme.aut.workout_tracker_backend.data_layer.workout.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private WorkoutService workoutService;

    private Workout workout;
    private WorkoutDTO workoutDTO;
    private Exercise exercise1;
    private Exercise exercise2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        exercise1 = new Exercise(1L, "Chest", "Barbell Bench Press");
        exercise2 = new Exercise(2L, "Legs", "Barbell Squat");

        workout = new Workout();
        workout.setId(1L);
        workout.setName("Workout 1");
        workout.setUserId("user@example.com");
        workout.setIsFavorite(true);
        workout.setExercises(Arrays.asList(1L, 2L));

        workoutDTO = new WorkoutDTO();
        workoutDTO.setId(1L);
        workoutDTO.setName("Workout 1");
        workoutDTO.setUserId("user@example.com");
        workoutDTO.setIsFavorite(true);
        workoutDTO.setExercises(Arrays.asList(exercise1, exercise2));
    }

    @Test
    void testUpdateWorkout() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));
        workoutService.updateWorkout(workoutDTO);

        ArgumentCaptor<Workout> workoutArgumentCaptor = ArgumentCaptor.forClass(Workout.class);
        verify(workoutRepository).save(workoutArgumentCaptor.capture());

        Workout capturedWorkout = workoutArgumentCaptor.getValue();
        assertThat(capturedWorkout.getName()).isEqualTo("Workout 1");
        assertThat(capturedWorkout.getUserId()).isEqualTo("user@example.com");
        assertThat(capturedWorkout.getIsFavorite()).isTrue();
        assertThat(capturedWorkout.getExercises()).containsExactly(1L, 2L);
    }

    @Test
    void testGetWorkout() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));
        when(exerciseService.getExercise(1L)).thenReturn(exercise1);
        when(exerciseService.getExercise(2L)).thenReturn(exercise2);

        WorkoutDTO result = workoutService.getWorkout(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Workout 1");
        assertThat(result.getUserId()).isEqualTo("user@example.com");
        assertThat(result.getIsFavorite()).isTrue();
        assertThat(result.getExercises()).containsExactly(exercise1, exercise2);
    }

    @Test
    void testGetWorkoutThrowsExceptionWhenNotFound() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> workoutService.getWorkout(1L));
    }

    @Test
    void testGetUserWorkouts() {
        when(workoutRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(Collections.singletonList(workout)));
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));
        when(exerciseService.getExercise(1L)).thenReturn(exercise1);
        when(exerciseService.getExercise(2L)).thenReturn(exercise2);

        List<WorkoutDTO> result = workoutService.getUserWorkouts("user@example.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Workout 1");
    }

    @Test
    void testGetUserFavoriteWorkouts() {
        when(workoutRepository.findByUserEmailAndIsFavorite("user@example.com")).thenReturn(Optional.of(Collections.singletonList(workout)));
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));
        when(exerciseService.getExercise(1L)).thenReturn(exercise1);
        when(exerciseService.getExercise(2L)).thenReturn(exercise2);

        List<WorkoutDTO> result = workoutService.getUserFavoriteWorkouts("user@example.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Workout 1");
    }

    @Test
    void testDeleteWorkout() {
        workoutService.deleteWorkout(1L);
        verify(workoutRepository).deleteById(1L);
    }
}
