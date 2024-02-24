package hu.bme.aut.workout_tracker_backend.data_layer.user;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String photo;
    @ElementCollection
    private List<Long> workouts = new ArrayList<>();
    @ElementCollection
    private List<Long> favoriteWorkouts = new ArrayList<>();
}
