package hu.bme.aut.workout_tracker_backend.data_layer.chart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class ChartRepositoryTest {

    @Autowired
    private ChartRepository chartRepository;

    private Chart chart1;
    private Chart chart2;

    @BeforeEach
    public void setUp() {
        chart1 = new Chart();
        chart1.setUserId("user1@example.com");

        chart2 = new Chart();
        chart2.setUserId("user1@example.com");

        chartRepository.save(chart1);
        chartRepository.save(chart2);
    }

    @Test
    public void testFindByUserId() {
        Optional<List<Chart>> found = chartRepository.findByUserId("user1@example.com");
        assertThat(found).isPresent();
        assertThat(found.get()).hasSize(2);
        assertThat(found.get()).contains(chart1, chart2);
    }

    @Test
    public void testFindByUserIdNotFound() {
        Optional<List<Chart>> found = chartRepository.findByUserId("nonexistent@example.com");
        assertThat(found).isPresent();
        assertThat(found.get()).hasSize(0);
    }
}
