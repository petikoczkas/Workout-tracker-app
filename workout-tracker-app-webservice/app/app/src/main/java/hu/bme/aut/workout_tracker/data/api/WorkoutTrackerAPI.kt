package hu.bme.aut.workout_tracker.data.api

import hu.bme.aut.workout_tracker.data.model.Chart
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.SavedExercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.data.model.auth.AuthResponse
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthLogin
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthRegister
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface WorkoutTrackerAPI {

    @GET("isAvailable")
    suspend fun isAvailable(): Result<Unit>

    //AUTH
    @POST("register")
    suspend fun registrate(@Body userAuthRegister: UserAuthRegister): Result<Unit>

    @POST("login")
    suspend fun signIn(@Body userAuthLogin: UserAuthLogin): Result<AuthResponse>

    //USER
    @GET("user/getCurrentUser")
    suspend fun getCurrentUser(
        @Header("Authorization") bearerToken: String,
        @Query("email") email: String
    ): User

    @GET("user/getUsers")
    suspend fun getUsers(
        @Header("Authorization") bearerToken: String,
    ): List<User>

    @POST("user/updateUser")
    suspend fun updateUser(
        @Header("Authorization") bearerToken: String,
        @Body user: User
    ): Result<Unit>

    //EXERCISE
    @GET("user/getExercises")
    suspend fun getExercises(
        @Header("Authorization") bearerToken: String,
    ): List<Exercise>

    @GET("user/getStandingsExercises")
    suspend fun getStandingsExercises(
        @Header("Authorization") bearerToken: String,
    ): List<Exercise>

    @POST("user/createExercise")
    suspend fun createExercise(
        @Header("Authorization") bearerToken: String,
        @Body exercise: Exercise
    ): Result<Unit>

    //WORKOUT
    @GET("user/getUserWorkouts")
    suspend fun getUserWorkouts(
        @Header("Authorization") bearerToken: String,
        @Query("email") email: String
    ): List<Workout>

    @GET("user/getUserFavoriteWorkouts")
    suspend fun getUserFavoriteWorkouts(
        @Header("Authorization") bearerToken: String,
        @Query("email") email: String
    ): List<Workout>

    @GET("user/getWorkout")
    suspend fun getWorkout(
        @Header("Authorization") bearerToken: String,
        @Query("id") workoutId: Int
    ): Workout

    @POST("user/updateWorkout")
    suspend fun updateWorkout(
        @Header("Authorization") bearerToken: String,
        @Body workout: Workout
    ): Result<Unit>

    @DELETE("user/deleteWorkout")
    suspend fun deleteWorkout(
        @Header("Authorization") bearerToken: String,
        @Query("id") workoutId: Int
    ): Result<Unit>

    //SAVED EXERCISE
    @GET("user/getUserSavedExercises")
    suspend fun getUserSavedExercises(
        @Header("Authorization") bearerToken: String,
        @Query("email") email: String
    ): List<SavedExercise>

    @POST("user/updateSavedExercise")
    suspend fun updateSavedExercise(
        @Header("Authorization") bearerToken: String,
        @Body savedExercise: SavedExercise
    ): Result<Unit>

    //CHART
    @GET("user/getUserCharts")
    suspend fun getUserCharts(
        @Header("Authorization") bearerToken: String,
        @Query("email") email: String
    ): List<Chart>

    @GET("user/getCharts")
    suspend fun getCharts(
        @Header("Authorization") bearerToken: String
    ): List<Chart>

    @POST("user/updateChart")
    suspend fun updateChart(
        @Header("Authorization") bearerToken: String,
        @Body chart: Chart
    ): Result<Unit>
}