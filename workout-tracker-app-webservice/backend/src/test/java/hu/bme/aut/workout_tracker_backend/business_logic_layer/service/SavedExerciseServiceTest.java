package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.SavedExerciseDTO;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.ExerciseRepository;
import hu.bme.aut.workout_tracker_backend.data_layer.saved_exercise.SavedExercise;
import hu.bme.aut.workout_tracker_backend.data_layer.saved_exercise.SavedExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SavedExerciseServiceTest {

    @Mock
    private SavedExerciseRepository savedExerciseRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private SavedExerciseService savedExerciseService;

    private SavedExercise savedExercise;
    private SavedExerciseDTO savedExerciseDTO;
    private Exercise exercise;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        exercise = new Exercise();
        exercise.setId(1L);

        savedExercise = new SavedExercise();
        savedExercise.setId(1L);
        savedExercise.setUserId("user@example.com");
        savedExercise.setExerciseId(1L);
        savedExercise.setData(Collections.singletonList("Sample data"));

        savedExerciseDTO = new SavedExerciseDTO();
        savedExerciseDTO.setId(1L);
        savedExerciseDTO.setUserId("user@example.com");
        savedExerciseDTO.setExercise(exercise);
        savedExerciseDTO.setData(Collections.singletonList("Sample data"));
    }

    @Test
    void testUpdateSavedExercise() {
        when(savedExerciseRepository.findById(1L)).thenReturn(Optional.of(savedExercise));

        savedExerciseService.updateSavedExercise(savedExerciseDTO);

        verify(savedExerciseRepository).save(savedExercise);
        assertThat(savedExercise.getUserId()).isEqualTo("user@example.com");
        assertThat(savedExercise.getExerciseId()).isEqualTo(1L);
        assertThat(savedExercise.getData()).containsExactly("Sample data");
    }

    @Test
    void testUpdateSavedExerciseCreatesNewWhenNotFound() {
        when(savedExerciseRepository.findById(1L)).thenReturn(Optional.empty());

        savedExerciseService.updateSavedExercise(savedExerciseDTO);

        verify(savedExerciseRepository).save(any(SavedExercise.class));
    }

    @Test
    void testGetUserSavedExercises() {
        when(savedExerciseRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(Collections.singletonList(savedExercise)));
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));

        List<SavedExerciseDTO> result = savedExerciseService.getUserSavedExercises("user@example.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo("user@example.com");
        assertThat(result.get(0).getExercise().getId()).isEqualTo(1L);
        assertThat(result.get(0).getData()).containsExactly("Sample data");
    }

    @Test
    void testGetUserSavedExercisesThrowsExceptionWhenUserNotFound() {
        when(savedExerciseRepository.findByUserEmail("user@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> savedExerciseService.getUserSavedExercises("user@example.com"));
    }
}
