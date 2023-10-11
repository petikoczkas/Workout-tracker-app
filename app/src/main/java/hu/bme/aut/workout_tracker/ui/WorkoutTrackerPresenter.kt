package hu.bme.aut.workout_tracker.ui

import android.net.Uri
import hu.bme.aut.workout_tracker.data.WorkoutTrackerInteractor
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import javax.inject.Inject

class WorkoutTrackerPresenter @Inject constructor(
    private val workoutTrackerInteractor: WorkoutTrackerInteractor
) {

    suspend fun registrate(
        email: String,
        password: String,
        user: User,
        onSuccess: () -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        workoutTrackerInteractor.registrate(
            email = email,
            password = password,
            user = user,
            onSuccess = {
                onSuccess()
            },
            onFailure = {
                onFailure(it)
            }
        )
    }

    suspend fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        workoutTrackerInteractor.signIn(
            email = email,
            password = password,
            onSuccess = {
                onSuccess()
            },
            onFailure = {
                onFailure(it)
            }
        )
    }

    fun signOut() = workoutTrackerInteractor.signOut()

    fun isLoggedIn() = workoutTrackerInteractor.isLoggedIn()

    fun getUsers() = workoutTrackerInteractor.getUsers()

    suspend fun getCurrentUser() = workoutTrackerInteractor.getCurrentUser()

    suspend fun updateUser(user: User) {
        workoutTrackerInteractor.updateUser(user = user)
    }

    suspend fun uploadProfilePicture(userId: String, imageUri: Uri, onSuccess: (String) -> Unit) {
        workoutTrackerInteractor.uploadProfilePicture(
            userId = userId,
            imageUri = imageUri,
            onSuccess = onSuccess
        )
    }

    fun getExercises() = workoutTrackerInteractor.getExercises()

    fun getStandingsExercises() = workoutTrackerInteractor.getStandingsExercises()


    fun getUserWorkouts(user: User) = workoutTrackerInteractor.getUserWorkouts(user = user)

    fun getUserFavoriteWorkouts(user: User) =
        workoutTrackerInteractor.getUserFavoriteWorkouts(user = user)

    fun getWorkoutExercises(workout: Workout) =
        workoutTrackerInteractor.getWorkoutExercises(workout = workout)

    suspend fun getWorkout(workoutId: String) =
        workoutTrackerInteractor.getWorkout(workoutId = workoutId)

    suspend fun createExercise(exercise: Exercise) {
        workoutTrackerInteractor.createExercise(exercise = exercise)
    }

    suspend fun updateWorkout(workout: Workout) {
        workoutTrackerInteractor.updateWorkout(workout = workout)
    }
}