package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.*;
import hu.bme.aut.workout_tracker_backend.data_layer.charts.ChartType;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.ExerciseRepository;
import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import hu.bme.aut.workout_tracker_backend.data_layer.user.UserRepository;
import hu.bme.aut.workout_tracker_backend.data_layer.user.exercises.SavedExercise;
import hu.bme.aut.workout_tracker_backend.data_layer.user.exercises.SavedExerciseRepository;
import hu.bme.aut.workout_tracker_backend.data_layer.workout.Workout;
import hu.bme.aut.workout_tracker_backend.data_layer.workout.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final SavedExerciseRepository savedExerciseRepository;
    private final ChartsService chartsService;

    public UserDataDTO getUserData(String email){
        val userWrapped = userRepository.findByEmail(email);
        if (userWrapped.isEmpty()) {
            throw new IllegalStateException("No such user");
        }
        var user = userWrapped.get();
        var data = new UserDataDTO();
        data.setEmail(user.getEmail());
        data.setFirstName(user.getFirstName());
        data.setLastName(user.getLastName());
        data.setPhoto(user.getPhoto());
        val workouts = workoutRepository.findByUserEmail(email);
        if(workouts.isPresent()){
            var workoutList = new ArrayList<WorkoutDTO>();
            for(Workout w: workouts.get()){
                var workoutDTO = new WorkoutDTO();
                workoutDTO.setId(w.getId());
                workoutDTO.setName(w.getName());
                workoutDTO.setIsFavorite(w.getIsFavorite());

                var exerciseList = new ArrayList<Exercise>();
                for(Long i: w.getExercises()){
                    var exercise = exerciseRepository.findById(i);
                    exercise.ifPresent(exerciseList::add);
                }
                workoutDTO.setExercises(exerciseList);
                workoutList.add(workoutDTO);
            }
            data.setWorkouts(workoutList);
        }
        val savedExercises = savedExerciseRepository.findByUserEmail(email);
        if(savedExercises.isPresent()){
            var exerciseList = new ArrayList<SavedExerciseDTO>();
            for(SavedExercise e: savedExercises.get()){
                var exerciseDTO = new SavedExerciseDTO();
                exerciseDTO.setId(e.getId());
                exerciseDTO.setData(e.getData());
                val exercise = exerciseRepository.findById(e.getExerciseId());
                exercise.ifPresent(exerciseDTO::setExercise);
                exerciseList.add(exerciseDTO);
            }
            data.setSavedExercises(exerciseList);
        }
        return data;
    }


    public UserSettingsDTO getUserSettingsData(String email) {
        val userWrapped = userRepository.findByEmail(email);
        if (userWrapped.isEmpty()) {
            throw new IllegalStateException("No such user");
        }
        var user = userWrapped.get();
        var data = new UserSettingsDTO();
        data.setEmail(user.getEmail());
        data.setFirstName(user.getFirstName());
        data.setLastName(user.getLastName());
        data.setPhoto(user.getPhoto());
        return data;
    }

    public List<UserStandingsDTO> getUserStandingsData() {
        val users = userRepository.findAll();
        val data = new ArrayList<UserStandingsDTO>();
        for (User u : users) {
            var userStandings = new UserStandingsDTO();
            userStandings.setEmail(u.getEmail());
            userStandings.setFirstName(u.getFirstName());
            userStandings.setLastName(u.getLastName());
            userStandings.setPhoto(u.getPhoto());

            val chartList = new ArrayList<ChartsDTO>();
            val charts = chartsService.getUserCharts(u.getEmail());
            for (ChartsDTO c : charts) {
                if (c.getType() == ChartType.OneRepMax) {
                    chartList.add(c);
                }
            }
            userStandings.setOneRepMaxCharts(chartList);
            data.add(userStandings);
        }
        return data;
    }
}
