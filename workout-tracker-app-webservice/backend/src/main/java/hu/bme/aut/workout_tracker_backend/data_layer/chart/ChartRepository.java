package hu.bme.aut.workout_tracker_backend.data_layer.chart;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChartRepository extends CrudRepository<Chart, Long> {

    @Query("SELECT c FROM Chart c WHERE c.userId = :id")
    Optional<List<Chart>> findByUserId(String id);
}
