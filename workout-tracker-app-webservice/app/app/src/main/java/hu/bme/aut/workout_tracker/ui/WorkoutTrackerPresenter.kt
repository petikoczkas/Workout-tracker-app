package hu.bme.aut.workout_tracker.ui

import android.graphics.Bitmap
import hu.bme.aut.workout_tracker.data.WorkoutTrackerInteractor
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.SavedExercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthLogin
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthRegister
import javax.inject.Inject

class WorkoutTrackerPresenter @Inject constructor(
    private val workoutTrackerInteractor: WorkoutTrackerInteractor
) {

    suspend fun registrate(
        userAuthRegister: UserAuthRegister,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        workoutTrackerInteractor.registrate(
            userAuthRegister = userAuthRegister,
            onSuccess = {
                onSuccess()
            },
            onFailure = {
                onFailure()
            }
        )
    }

    suspend fun signIn(
        userAuthLogin: UserAuthLogin,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        workoutTrackerInteractor.signIn(
            userAuthLogin = userAuthLogin,
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

    suspend fun getCurrentUser() = workoutTrackerInteractor.getCurrentUser()

    suspend fun getUsers() = workoutTrackerInteractor.getUsers()

    suspend fun updateUser(user: User) {
        workoutTrackerInteractor.updateUser(user = user)
    }

    suspend fun uploadProfilePicture(user: User, imageBitmap: Bitmap, onSuccess: (String) -> Unit) {
        workoutTrackerInteractor.uploadProfilePicture(
            user = user,
            imageBitmap = imageBitmap,
            onSuccess = onSuccess
        )
    }

    suspend fun getExercises() = workoutTrackerInteractor.getExercises()

    suspend fun getStandingsExercises() = workoutTrackerInteractor.getStandingsExercises()

    suspend fun createExercise(exercise: Exercise) {
        workoutTrackerInteractor.createExercise(exercise = exercise)
    }

    suspend fun getUserWorkouts(email: String) =
        workoutTrackerInteractor.getUserWorkouts(email = email)

    suspend fun getUserFavoriteWorkouts(email: String) =
        workoutTrackerInteractor.getUserFavoriteWorkouts(email = email)

    suspend fun getWorkout(workoutId: String) =
        workoutTrackerInteractor.getWorkout(workoutId = workoutId)

    suspend fun updateWorkout(workout: Workout) {
        workoutTrackerInteractor.updateWorkout(workout = workout)
    }

    suspend fun getUserSavedExercises(email: String) =
        workoutTrackerInteractor.getUserSavedExercises(email = email)

    suspend fun updateSavedExercise(savedExercise: SavedExercise) =
        workoutTrackerInteractor.updateSavedExercise(savedExercise = savedExercise)

    suspend fun getUserCharts(email: String) =
        workoutTrackerInteractor.getUserCharts(email = email)

    suspend fun updateChart(chart: Chart) =
        workoutTrackerInteractor.updateChart(chart = chart)
}