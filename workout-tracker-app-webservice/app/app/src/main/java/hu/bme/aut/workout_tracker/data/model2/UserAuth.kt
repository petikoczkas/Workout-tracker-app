package hu.bme.aut.workout_tracker.data.model2


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserAuth(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)