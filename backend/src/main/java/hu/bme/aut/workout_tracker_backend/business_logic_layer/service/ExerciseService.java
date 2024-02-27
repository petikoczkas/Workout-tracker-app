package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public void createExercise(Exercise exercise) {
        exerciseRepository.save(exercise);
    }
}
