package hu.bme.aut.workout_tracker.data.api

import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.UserAuth
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface WorkoutTrackerAPI {

    //AUTH
    @POST("register")
    suspend fun registration(@Body user: User)

    @POST("login")
    suspend fun login(@Body userAuth: UserAuth): String

    //USER
    @GET("user/getCurrentUser")
    suspend fun getCurrentUser(
        @Header("Authorization") bearerToken: String,
        @Query("email") email: String
    ): User
}