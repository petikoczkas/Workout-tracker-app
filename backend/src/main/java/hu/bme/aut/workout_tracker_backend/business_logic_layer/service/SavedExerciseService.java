package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.data_layer.user.exercises.SavedExercise;
import hu.bme.aut.workout_tracker_backend.data_layer.user.exercises.SavedExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavedExerciseService {

    private final SavedExerciseRepository savedExerciseRepository;

    public void createSavedExercise(SavedExercise savedExercise){
        savedExerciseRepository.save(savedExercise);
    }
}
