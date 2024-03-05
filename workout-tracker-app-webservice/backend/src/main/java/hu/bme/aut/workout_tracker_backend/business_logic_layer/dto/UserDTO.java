package hu.bme.aut.workout_tracker_backend.business_logic_layer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    String email;
    String firstName;
    String lastName;
    String photo;
    /*List<WorkoutDTO> workouts = new ArrayList<>();
    List<SavedExerciseDTO> savedExercises = new ArrayList<>();
    List<ChartDTO> charts = new ArrayList<>();*/
}
