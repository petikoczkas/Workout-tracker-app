package hu.bme.aut.workout_tracker_backend.data_layer.saved_exercise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedExercise {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native")
    private Long id;
    private String userId;
    private Long exerciseId;
    @ElementCollection
    private List<String> data = new ArrayList<>();
}
