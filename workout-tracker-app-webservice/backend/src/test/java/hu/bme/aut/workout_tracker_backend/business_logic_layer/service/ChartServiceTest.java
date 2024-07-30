package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.ChartDTO;
import hu.bme.aut.workout_tracker_backend.data_layer.chart.Chart;
import hu.bme.aut.workout_tracker_backend.data_layer.chart.ChartRepository;
import hu.bme.aut.workout_tracker_backend.data_layer.chart.ChartType;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.ExerciseRepository;
import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import hu.bme.aut.workout_tracker_backend.data_layer.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ChartServiceTest {

    @Mock
    private ChartRepository chartRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChartService chartService;

    private Chart chart1;
    private Chart chart2;
    private Exercise exercise1;
    private User user1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exercise1 = new Exercise();
        exercise1.setId(1L);

        user1 = new User();
        user1.setEmail("user@example.com");

        chart1 = new Chart();
        chart1.setId(1L);
        chart1.setUserId("user@example.com");
        chart1.setExerciseId(1L);
        chart1.setType(ChartType.Volume);
        chart1.setData(Collections.singletonList(1.0));

        chart2 = new Chart();
        chart2.setId(2L);
        chart2.setUserId("user@example.com");
        chart2.setExerciseId(1L);
        chart2.setType(ChartType.OneRepMax);
        chart2.setData(Collections.singletonList(2.0));
    }

    @Test
    void testUpdateChart_NewChart() {
        ChartDTO chartDTO = new ChartDTO();
        chartDTO.setUserId("user@example.com");
        chartDTO.setExercise(exercise1);
        chartDTO.setType(ChartType.Volume);
        chartDTO.setData(Collections.singletonList(1.0));

        chartService.updateChart(chartDTO);

        verify(chartRepository, times(1)).save(any(Chart.class));
    }

    @Test
    void testUpdateChart_ExistingChart() {
        ChartDTO chartDTO = new ChartDTO();
        chartDTO.setId(1L);
        chartDTO.setUserId("user@example.com");
        chartDTO.setExercise(exercise1);
        chartDTO.setType(ChartType.Volume);
        chartDTO.setData(Collections.singletonList(1.0));

        when(chartRepository.findById(1L)).thenReturn(Optional.of(chart1));

        chartService.updateChart(chartDTO);

        verify(chartRepository, times(1)).save(chart1);
    }

    @Test
    void testGetUserCharts() {
        when(chartRepository.findByUserId("user@example.com")).thenReturn(Optional.of(List.of(chart1, chart2)));
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise1));

        List<ChartDTO> result = chartService.getUserCharts("user@example.com");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getType()).isEqualTo(ChartType.Volume);
        assertThat(result.get(1).getType()).isEqualTo(ChartType.OneRepMax);
        verify(chartRepository, times(1)).findByUserId("user@example.com");
    }

    @Test
    void testGetCharts() {
        when(userRepository.findAll()).thenReturn(List.of(user1));
        when(chartRepository.findByUserId("user@example.com")).thenReturn(Optional.of(List.of(chart1, chart2)));
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise1));

        List<ChartDTO> result = chartService.getCharts();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getType()).isEqualTo(ChartType.Volume);
        assertThat(result.get(1).getType()).isEqualTo(ChartType.OneRepMax);
        verify(userRepository, times(1)).findAll();
    }
}
