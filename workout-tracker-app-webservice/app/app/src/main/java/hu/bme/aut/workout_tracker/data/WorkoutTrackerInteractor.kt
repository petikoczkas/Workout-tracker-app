package hu.bme.aut.workout_tracker.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.bme.aut.workout_tracker.data.api.WorkoutTrackerAPI
import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.SavedExercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthLogin
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthRegister
import hu.bme.aut.workout_tracker.data.util.ByteArrayAdapter
import hu.bme.aut.workout_tracker.data.util.ResultConverterFactory
import hu.bme.aut.workout_tracker.utils.Constants.currentUserEmail
import hu.bme.aut.workout_tracker.utils.Constants.token
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class WorkoutTrackerInteractor @Inject constructor() {

    private val workoutTrackerAPI: WorkoutTrackerAPI

    init {
        val moshi = Moshi.Builder()
            .add(ByteArrayAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val intercepter = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(intercepter)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.106:8080/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(ResultConverterFactory.create(moshi))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

        workoutTrackerAPI = retrofit.create(WorkoutTrackerAPI::class.java)
    }

    fun registrate(
        userAuthRegister: UserAuthRegister,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                workoutTrackerAPI.registrate(userAuthRegister)
                    .onSuccess {
                        signIn(
                            UserAuthLogin(userAuthRegister.email, userAuthRegister.password),
                            onSuccess,
                            onFailure
                        )
                    }
                    .onFailure {
                        onFailure(Exception(it.message))

                    }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun signIn(
        userAuthLogin: UserAuthLogin,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                workoutTrackerAPI.signIn(userAuthLogin)
                    .onSuccess {
                        token = "Bearer ${it.token}"
                        currentUserEmail = userAuthLogin.email.lowercase()
                        onSuccess(it.token)
                    }
                    .onFailure {
                        onFailure(Exception(it.message))

                    }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun signOut() {
        token = ""
        currentUserEmail = ""
    }

    suspend fun getCurrentUser(): User {
        return workoutTrackerAPI.getCurrentUser(
            bearerToken = token,
            email = currentUserEmail
        )
    }


    suspend fun getUsers() = workoutTrackerAPI.getUsers(bearerToken = token)

    suspend fun updateUser(
        user: User,
        onSuccess: () -> Unit
    ) {
        workoutTrackerAPI.updateUser(
            bearerToken = token,
            user = user
        ).onSuccess {
            onSuccess()
        }.onFailure {

        }
    }

    suspend fun getExercises() = workoutTrackerAPI.getExercises(bearerToken = token)

    suspend fun getStandingsExercises() =
        workoutTrackerAPI.getStandingsExercises(bearerToken = token).sortedBy { it.name }

    suspend fun createExercise(exercise: Exercise) {
        workoutTrackerAPI.createExercise(
            bearerToken = token,
            exercise = exercise
        )
    }

    suspend fun getUserWorkouts(email: String) =
        workoutTrackerAPI.getUserWorkouts(
            bearerToken = token,
            email = email
        )

    suspend fun getUserFavoriteWorkouts(email: String) =
        workoutTrackerAPI.getUserFavoriteWorkouts(
            bearerToken = token,
            email = email
        )

    suspend fun getWorkout(workoutId: Int) =
        workoutTrackerAPI.getWorkout(
            bearerToken = token,
            workoutId = workoutId
        )

    suspend fun updateWorkout(workout: Workout) {
        workoutTrackerAPI.updateWorkout(
            bearerToken = token,
            workout = workout
        )
    }

    suspend fun deleteWorkout(workoutId: Int) =
        workoutTrackerAPI.deleteWorkout(
            bearerToken = token,
            workoutId = workoutId
        )

    suspend fun getUserSavedExercises(email: String) =
        workoutTrackerAPI.getUserSavedExercises(
            bearerToken = token,
            email = email
        )

    suspend fun updateSavedExercise(savedExercise: SavedExercise) {
        workoutTrackerAPI.updateSavedExercise(
            bearerToken = token,
            savedExercise = savedExercise
        )
    }

    suspend fun getUserCharts(email: String) =
        workoutTrackerAPI.getUserCharts(
            bearerToken = token,
            email = email
        )

    suspend fun getCharts() =
        workoutTrackerAPI.getCharts(
            bearerToken = token
        )

    suspend fun updateChart(chart: Chart) {
        workoutTrackerAPI.updateChart(
            bearerToken = token,
            chart = chart
        )
    }
}