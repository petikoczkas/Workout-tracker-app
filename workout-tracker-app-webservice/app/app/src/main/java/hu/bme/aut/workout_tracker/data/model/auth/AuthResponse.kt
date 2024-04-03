package hu.bme.aut.workout_tracker.data.model.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class AuthResponse(
    @Json(name = "token")
    val token: String,
)
