package hu.bme.aut.workout_tracker.data.model.auth


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserAuthLogin(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)