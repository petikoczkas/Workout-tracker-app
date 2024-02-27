package hu.bme.aut.workout_tracker_backend.business_logic_layer.dto;

import hu.bme.aut.workout_tracker_backend.data_layer.charts.ChartType;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChartsDTO {
    String userId;
    Exercise exercise;
    ChartType type;
    List<String> data = new ArrayList<>();
}
