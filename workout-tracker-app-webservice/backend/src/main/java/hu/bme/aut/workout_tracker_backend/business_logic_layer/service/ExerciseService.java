package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.api.ApiConstants;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public void createExercise(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    public Exercise getExercise(Long id) {
        val exercise = exerciseRepository.findById(id);
        if (exercise.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ApiConstants.exerciseNotFound);
        }
        return exercise.get();
    }

    public List<Exercise> getExercises() {
        val list = new ArrayList<Exercise>();
        exerciseRepository.findAll().forEach(list::add);
        return list;
    }

    public List<Exercise> getStandingsExercises() {
        val list = new ArrayList<Exercise>();
        exerciseRepository.findAll().forEach(list::add);
        return list.stream().filter(e -> ApiConstants.standingExerciseList.contains(e.getName())).toList();
    }
}
