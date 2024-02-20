package hu.bme.aut.workout_tracker_backend.data_layer.user.exercises;

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
public class Exercises {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native")
    private Long id;
    private String userId;
    private Long exerciseId;
    @ElementCollection
    private List<String> data = new ArrayList<>();
}