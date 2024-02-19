package hu.bme.aut.workout_tracker_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "hu.bme.aut.workout_tracker_backend")
public class WorkoutTrackerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkoutTrackerBackendApplication.class, args);
	}

}
