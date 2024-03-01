package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.SavedExerciseDTO;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.ExerciseRepository;
import hu.bme.aut.workout_tracker_backend.data_layer.saved_exercise.SavedExercise;
import hu.bme.aut.workout_tracker_backend.data_layer.saved_exercise.SavedExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedExerciseService {

    private final SavedExerciseRepository savedExerciseRepository;
    private final ExerciseRepository exerciseRepository;

    public void updateSavedExercise(SavedExerciseDTO e) {
        var exercise = new SavedExercise();
        if (e.getId() != null) {
            val savedExerciseWrapped = savedExerciseRepository.findById(e.getId());
            if (savedExerciseWrapped.isPresent()) exercise = savedExerciseWrapped.get();
        }
        exercise.setUserId(e.getUserId());
        exercise.setExerciseId(e.getExercise().getId());
        exercise.setData(e.getData());
        savedExerciseRepository.save(exercise);
    }

    public List<SavedExerciseDTO> getUserSavedExercises(String email) {
        val savedExercises = savedExerciseRepository.findByUserEmail(email);
        if (savedExercises.isEmpty()) {
            throw new IllegalStateException("No such User");
        }
        var data = new ArrayList<SavedExerciseDTO>();
        for (SavedExercise e : savedExercises.get()) {
            var exerciseDTO = new SavedExerciseDTO();
            exerciseDTO.setId(e.getId());
            exerciseDTO.setUserId(e.getUserId());
            exerciseDTO.setData(e.getData());
            val exercise = exerciseRepository.findById(e.getExerciseId());
            exercise.ifPresent(exerciseDTO::setExercise);
            data.add(exerciseDTO);
        }
        return data;
    }
}
