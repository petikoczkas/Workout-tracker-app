package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.WorkoutDTO;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.workout.Workout;
import hu.bme.aut.workout_tracker_backend.data_layer.workout.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final ExerciseService exerciseService;

    public void updateWorkout(WorkoutDTO w) {
        var workout = new Workout();
        if (w.getId() != null) {
            val workoutWrapped = workoutRepository.findById(w.getId());
            if (workoutWrapped.isPresent()) workout = workoutWrapped.get();
        }
        workout.setName(w.getName());
        workout.setUserId(w.getUserId());
        workout.setIsFavorite(w.getIsFavorite());
        var exerciseList = new ArrayList<Long>();
        for (Exercise e : w.getExercises()) {
            exerciseList.add(e.getId());
        }
        workout.setExercises(exerciseList);
        workoutRepository.save(workout);
    }

    public WorkoutDTO getWorkout(Long id) {
        val workoutWrapped = workoutRepository.findById(id);
        if (workoutWrapped.isEmpty()) {
            throw new IllegalStateException("No such workout");
        }

        val workout = workoutWrapped.get();
        var data = new WorkoutDTO();
        data.setId(workout.getId());
        data.setUserId(workout.getUserId());
        data.setName(workout.getName());
        data.setIsFavorite(workout.getIsFavorite());

        var exerciseList = new ArrayList<Exercise>();
        for (Long i : workout.getExercises()) {
            exerciseList.add(exerciseService.getExercise(i));
        }
        data.setExercises(exerciseList);
        return data;
    }

    public List<WorkoutDTO> getUserWorkouts(String email) {
        val listWrapped = workoutRepository.findByUserEmail(email);
        val list = new ArrayList<WorkoutDTO>();
        if (listWrapped.isPresent()) {
            for (Workout w : listWrapped.get()) {
                list.add(getWorkout(w.getId()));
            }
        }
        return list;
    }

    public List<WorkoutDTO> getUserFavoriteWorkouts(String email) {
        val listWrapped = workoutRepository.findByUserEmailAndIsFavorite(email);
        val list = new ArrayList<WorkoutDTO>();
        if (listWrapped.isPresent()) {
            for (Workout w : listWrapped.get()) {
                list.add(getWorkout(w.getId()));
            }
        }
        return list;
    }
}
