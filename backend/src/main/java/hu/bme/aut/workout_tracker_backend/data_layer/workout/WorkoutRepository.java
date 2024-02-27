package hu.bme.aut.workout_tracker_backend.data_layer.workout;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends CrudRepository<Workout, Long> {

    @Query("SELECT w FROM Workout w WHERE w.userId = :email")
    Optional<List<Workout>> findByUserEmail(String email);
}
