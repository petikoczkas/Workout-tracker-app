package hu.bme.aut.workout_tracker_backend.business_logic_layer.api;

import java.util.Arrays;
import java.util.List;

public class ApiConstants {
    public static String userRole = "ROLE_USER";
    public static String invalidCredentialsMessage = "Invalid user credentials!";
    public static String userNotFoundWithEmailMessage = "User not found with email:";
    public static String exerciseNotFound = "Exercise not found";
    public static String workoutNotFound = "Workout not found";
    public static String userAlreadyExistsMessage = "User already exists!";
    public static String userAddedMessage = "User Added Successfully!";
    public static List<String> standingExerciseList = Arrays.asList("Barbell Bench Press", "Barbell Deadlift", "Barbell Squat");
}
