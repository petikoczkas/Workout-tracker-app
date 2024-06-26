package hu.bme.aut.workout_tracker_backend.business_logic_layer.dto;

import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class WorkoutDTO {
    Long id;
    String userId;
    String name;
    Boolean isFavorite;
    List<Exercise> exercises = new ArrayList<>();
}
