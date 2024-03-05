package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.ChartDTO;
import hu.bme.aut.workout_tracker_backend.data_layer.chart.Chart;
import hu.bme.aut.workout_tracker_backend.data_layer.chart.ChartRepository;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final ChartRepository chartRepository;
    private final ExerciseRepository exerciseRepository;

    public void updateChart(ChartDTO c) {
        var chart = new Chart();
        if (c.getId() != null) {
            val chartWrapped = chartRepository.findById(c.getId());
            if (chartWrapped.isPresent()) chart = chartWrapped.get();
        }
        chart.setUserId(c.getUserId());
        chart.setExerciseId(c.getExercise().getId());
        chart.setType(c.getType());
        chart.setData(c.getData());
        chartRepository.save(chart);
    }

    public List<ChartDTO> getUserCharts(String email) {
        val listWrapped = chartRepository.findByUserId(email);
        val list = new ArrayList<ChartDTO>();
        if (listWrapped.isPresent()) {
            for (Chart c : listWrapped.get()) {
                var chart = new ChartDTO();
                chart.setId(c.getId());
                chart.setType(c.getType());
                chart.setUserId(c.getUserId());
                chart.setData(c.getData());
                val exerciseWrapped = exerciseRepository.findById(c.getExerciseId());
                exerciseWrapped.ifPresent(chart::setExercise);
                list.add(chart);
            }
        }
        return list;
    }
}
