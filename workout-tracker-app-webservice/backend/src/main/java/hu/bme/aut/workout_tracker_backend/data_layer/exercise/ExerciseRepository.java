package hu.bme.aut.workout_tracker_backend.data_layer.exercise;

import org.springframework.data.repository.CrudRepository;

public interface ExerciseRepository extends CrudRepository<Exercise, Long> {

}
