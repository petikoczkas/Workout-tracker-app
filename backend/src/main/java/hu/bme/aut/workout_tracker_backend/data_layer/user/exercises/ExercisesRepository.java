package hu.bme.aut.workout_tracker_backend.data_layer.user.exercises;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExercisesRepository extends CrudRepository<Exercises, Long> {
    @Query("SELECT e FROM Exercises e WHERE e.userId = :userId and e.exerciseId = :exerciseId")
    Optional<Exercises> findUserExerciseById(String userId, Long exerciseId);
}
