package hu.bme.aut.workout_tracker.ui

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

    fun registrate(
        userAuthRegister: UserAuthRegister,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        workoutTrackerInteractor.registrate(
            userAuthRegister = userAuthRegister,
            onSuccess = {
                onSuccess(it)
            },
            onFailure = {
                onFailure(it)
            }
        )
    }

    fun signIn(
        userAuthLogin: UserAuthLogin,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        workoutTrackerInteractor.signIn(
            userAuthLogin = userAuthLogin,
            onSuccess = {
                onSuccess(it)
            },
            onFailure = {
                onFailure(it)
            }
        )
    }

    fun signOut() = workoutTrackerInteractor.signOut()

    suspend fun getCurrentUser() = workoutTrackerInteractor.getCurrentUser()

    suspend fun getUsers() = workoutTrackerInteractor.getUsers()

    suspend fun updateUser(user: User, onSuccess: () -> Unit) {
        workoutTrackerInteractor.updateUser(user = user, onSuccess = onSuccess)
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

    suspend fun getWorkout(workoutId: Int) =
        workoutTrackerInteractor.getWorkout(workoutId = workoutId)

    suspend fun updateWorkout(workout: Workout) {
        workoutTrackerInteractor.updateWorkout(workout = workout)
    }

    suspend fun deleteWorkout(workoutId: Int) =
        workoutTrackerInteractor.deleteWorkout(workoutId = workoutId)

    suspend fun getUserSavedExercises(email: String) =
        workoutTrackerInteractor.getUserSavedExercises(email = email)

    suspend fun updateSavedExercise(savedExercise: SavedExercise) =
        workoutTrackerInteractor.updateSavedExercise(savedExercise = savedExercise)

    suspend fun getUserCharts(email: String) =
        workoutTrackerInteractor.getUserCharts(email = email)

    suspend fun getCharts() =
        workoutTrackerInteractor.getCharts()

    suspend fun updateChart(chart: Chart) =
        workoutTrackerInteractor.updateChart(chart = chart)
}