package hu.bme.aut.workout_tracker_backend.business_logic_layer.api;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication.AuthRequest;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication.AuthTokenService;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.ChartDTO;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.SavedExerciseDTO;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.UserDTO;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.WorkoutDTO;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.service.*;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ApiController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;

    private final UserService userService;
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;
    private final SavedExerciseService savedExerciseService;
    private final ChartService chartService;

    // AUTH
    @PostMapping("/register")
    public String addNewUser(@RequestBody User user) {
        user.setRoles(ApiConstants.userRole);
        return authenticationService.addUser(user);
    }

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return authTokenService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException(ApiConstants.invalidCredentialsMessage);
        }
    }

    //USER
    @GetMapping("/user/getCurrentUser")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public UserDTO getCurrentUser(@RequestParam String email) {
        return userService.getUser(email);
    }

    @PostMapping("/user/updateUser")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void updateUser(@RequestBody UserDTO user) {
        userService.updateUser(user);
    }

    @GetMapping("/user/getUsers")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    //EXERCISE
    @GetMapping("/user/getExercises")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Exercise> getExercises() {
        return exerciseService.getExercises();
    }

    @GetMapping("/user/getStandingsExercises")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Exercise> getStandingsExercises() {
        return exerciseService.getStandingsExercises();
    }

    @PostMapping("/user/createExercise")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void createExercise(@RequestBody Exercise exercise) {
        exerciseService.createExercise(exercise);
    }

    //WORKOUT
    @GetMapping("/user/getUserWorkouts")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<WorkoutDTO> getUserWorkouts(@RequestParam String email) {
        return workoutService.getUserWorkouts(email);
    }

    @GetMapping("/user/getUserFavoriteWorkouts")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<WorkoutDTO> getUserFavoriteWorkouts(@RequestParam String email) {
        return workoutService.getUserFavoriteWorkouts(email);
    }

    @GetMapping("/user/getWorkout")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public WorkoutDTO getWorkout(@RequestParam Long id) {
        return workoutService.getWorkout(id);
    }

    @PostMapping("/user/updateWorkout")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void updateWorkout(@RequestBody WorkoutDTO workout) {
        workoutService.updateWorkout(workout);
    }

    //SAVED EXERCISE
    @GetMapping("/user/getUserSavedExercises")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public List<SavedExerciseDTO> getUserSavedExercises(@RequestParam String email) {
        return savedExerciseService.getUserSavedExercises(email);
    }

    @PostMapping("/user/updateSavedExercise")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void updateSavedExercise(@RequestBody SavedExerciseDTO savedExercise) {
        savedExerciseService.updateSavedExercise(savedExercise);
    }

    //CHART
    @GetMapping("/user/getUserCharts")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public List<ChartDTO> getUserCharts(@RequestParam String email) {
        return chartService.getUserCharts(email);
    }

    @PostMapping("/user/updateChart")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void updateChart(@RequestBody ChartDTO chart) {
        chartService.updateChart(chart);
    }
}
