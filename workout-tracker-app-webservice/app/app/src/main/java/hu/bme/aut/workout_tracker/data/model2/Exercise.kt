package hu.bme.aut.workout_tracker.data.model2


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Exercise(
    @Json(name = "id")
    val id: Int,
    @Json(name = "category")
    val category: String,
    @Json(name = "name")
    val name: String
)