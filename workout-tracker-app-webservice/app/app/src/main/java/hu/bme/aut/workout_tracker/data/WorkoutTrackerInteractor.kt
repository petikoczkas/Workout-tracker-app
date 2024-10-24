package hu.bme.aut.workout_tracker.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.bme.aut.workout_tracker.BuildConfig
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
import hu.bme.aut.workout_tracker.utils.AppData.currentUserEmail
import hu.bme.aut.workout_tracker.utils.AppData.token
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

        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(ResultConverterFactory.create(moshi))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

        workoutTrackerAPI = retrofit.create(WorkoutTrackerAPI::class.java)
    }

    fun isAvailable(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                workoutTrackerAPI.isAvailable().onSuccess { onSuccess() }.onFailure { onFailure() }
            } catch (e: Exception) {
                onFailure()
            }
        }
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
        return try {
            workoutTrackerAPI.getCurrentUser(
                bearerToken = token,
                email = currentUserEmail
            )
        } catch (_: Exception) {
            User()
        }
    }


    suspend fun getUsers() = try {
        workoutTrackerAPI.getUsers(bearerToken = token)
    } catch (_: Exception) {
        emptyList()
    }

    suspend fun updateUser(
        user: User,
        onSuccess: () -> Unit
    ) {
        try {
            workoutTrackerAPI.updateUser(
                bearerToken = token,
                user = user
            ).onSuccess {
                onSuccess()
            }
        } catch (_: Exception) {

        }
    }

    suspend fun getExercises() = try {
        workoutTrackerAPI.getExercises(bearerToken = token)
    } catch (_: Exception) {
        emptyList()
    }

    suspend fun getStandingsExercises() =
        try {
            workoutTrackerAPI.getStandingsExercises(bearerToken = token).sortedBy { it.name }
        } catch (_: Exception) {
            emptyList()
        }

    suspend fun createExercise(exercise: Exercise) {
        try {
            workoutTrackerAPI.createExercise(
                bearerToken = token,
                exercise = exercise
            )
        } catch (_: Exception) {
        }
    }

    suspend fun getUserWorkouts(email: String) =
        try {
            workoutTrackerAPI.getUserWorkouts(
                bearerToken = token,
                email = email
            )
        } catch (_: Exception) {
            emptyList()
        }

    suspend fun getUserFavoriteWorkouts(email: String) =
        try {
            workoutTrackerAPI.getUserFavoriteWorkouts(
                bearerToken = token,
                email = email
            )
        } catch (_: Exception) {
            emptyList()
        }

    suspend fun getWorkout(workoutId: Int) =
        try {
            workoutTrackerAPI.getWorkout(
                bearerToken = token,
                workoutId = workoutId
            )
        } catch (_: Exception) {
            Workout()
        }

    suspend fun updateWorkout(workout: Workout) {
        try {
            workoutTrackerAPI.updateWorkout(
                bearerToken = token,
                workout = workout
            )
        } catch (_: Exception) {
        }
    }

    suspend fun deleteWorkout(workoutId: Int) =
        try {
            workoutTrackerAPI.deleteWorkout(
                bearerToken = token,
                workoutId = workoutId
            )
        } catch (_: Exception) {
        }

    suspend fun getUserSavedExercises(email: String) =
        try {
            workoutTrackerAPI.getUserSavedExercises(
                bearerToken = token,
                email = email
            )
        } catch (_: Exception) {
            emptyList()
        }

    suspend fun updateSavedExercise(savedExercise: SavedExercise) {
        try {
            workoutTrackerAPI.updateSavedExercise(
                bearerToken = token,
                savedExercise = savedExercise
            )
        } catch (_: Exception) {
        }
    }

    suspend fun getUserCharts(email: String) =
        try {
            workoutTrackerAPI.getUserCharts(
                bearerToken = token,
                email = email
            )
        } catch (_: Exception) {
            emptyList()
        }

    suspend fun getCharts() =
        try {
            workoutTrackerAPI.getCharts(
                bearerToken = token
            )
        } catch (_: Exception) {
            emptyList()
        }

    suspend fun updateChart(chart: Chart) {
        try {
            workoutTrackerAPI.updateChart(
                bearerToken = token,
                chart = chart
            )
        } catch (_: Exception) {
        }
    }
}