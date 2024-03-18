package hu.bme.aut.workout_tracker.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Workout(
    @Json(name = "id")
    val id: Int,
    @Json(name = "userId")
    val userId: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "isFavorite")
    val isFavorite: Boolean,
    @Json(name = "exercises")
    val exercises: ArrayList<Exercise>
)