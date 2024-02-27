package hu.bme.aut.workout_tracker_backend.business_logic_layer.api;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication.AuthRequest;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication.AuthTokenService;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.UserDataDTO;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.service.*;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import hu.bme.aut.workout_tracker_backend.data_layer.user.exercises.SavedExercise;
import hu.bme.aut.workout_tracker_backend.data_layer.workout.Workout;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user/getUserData")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public UserDataDTO getUserData(@RequestParam String email) {
        return userService.getUserData(email);
    }

    //with id you can use it to edit it
    @PostMapping("/user/createWorkout")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void createWorkout(@RequestBody Workout workout) {
        workoutService.createWorkout(workout);
    }

    @PostMapping("/user/createExercise")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void createExercise(@RequestBody Exercise exercise) {
        exerciseService.createExercise(exercise);
    }

    @PostMapping("/user/createSavedExercise")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void createSavedExercise(@RequestBody SavedExercise savedExercise) {
        savedExerciseService.createSavedExercise(savedExercise);
    }


}
