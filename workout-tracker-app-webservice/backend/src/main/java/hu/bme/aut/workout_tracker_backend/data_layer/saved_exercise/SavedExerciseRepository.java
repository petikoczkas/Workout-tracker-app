package hu.bme.aut.workout_tracker_backend.data_layer.saved_exercise;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedExerciseRepository extends CrudRepository<SavedExercise, Long> {
    @Query("SELECT e FROM SavedExercise e WHERE e.userId = :email")
    Optional<List<SavedExercise>> findByUserEmail(String email);
}
