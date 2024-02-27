package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.data_layer.workout.Workout;
import hu.bme.aut.workout_tracker_backend.data_layer.workout.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public void createWorkout(Workout workout){
        workoutRepository.save(workout);
    }
}
