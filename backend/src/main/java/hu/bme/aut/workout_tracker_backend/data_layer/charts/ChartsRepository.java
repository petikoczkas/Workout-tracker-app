package hu.bme.aut.workout_tracker_backend.data_layer.charts;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChartsRepository extends CrudRepository<Charts, Long> {

    @Query("SELECT c FROM Charts c WHERE c.userId = :id")
    Optional<List<Charts>> findByUserId(String id);
}
