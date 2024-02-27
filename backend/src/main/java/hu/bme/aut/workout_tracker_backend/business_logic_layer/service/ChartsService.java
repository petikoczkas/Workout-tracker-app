package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.ChartsDTO;
import hu.bme.aut.workout_tracker_backend.data_layer.charts.Charts;
import hu.bme.aut.workout_tracker_backend.data_layer.charts.ChartsRepository;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartsService {

    private final ChartsRepository chartsRepository;
    private final ExerciseRepository exerciseRepository;

    public List<ChartsDTO> getUserCharts(String email) {
        val listWrapped = chartsRepository.findByUserId(email);
        val list = new ArrayList<ChartsDTO>();
        if (listWrapped.isPresent()) {
            for (Charts c : listWrapped.get()) {
                var chart = new ChartsDTO();
                chart.setUserId(c.getUserId());
                chart.setType(c.getType());
                chart.setData(c.getData());
                val exerciseWrapped = exerciseRepository.findById(c.getExerciseId());
                exerciseWrapped.ifPresent(chart::setExercise);
                list.add(chart);
            }
        }
        return list;
    }
}
