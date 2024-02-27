package hu.bme.aut.workout_tracker_backend.business_logic_layer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDataDTO {
    String email;
    String firstName;
    String lastName;
    String photo;
    List<WorkoutDTO> workouts = new ArrayList<>();
    List<SavedExerciseDTO> savedExercises = new ArrayList<>();
}
