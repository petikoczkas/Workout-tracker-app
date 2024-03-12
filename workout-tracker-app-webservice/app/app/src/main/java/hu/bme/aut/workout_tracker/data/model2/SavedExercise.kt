package hu.bme.aut.workout_tracker.data.model2


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SavedExercise(
    @Json(name = "id")
    val id: Int,
    @Json(name = "userId")
    val userId: String,
    @Json(name = "exercise")
    val exercise: Exercise,
    @Json(name = "data")
    val data: ArrayList<String>
)