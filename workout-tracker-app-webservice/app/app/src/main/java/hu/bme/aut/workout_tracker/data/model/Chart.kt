package hu.bme.aut.workout_tracker.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Chart(
    @Json(name = "id")
    val id: Int,
    @Json(name = "userId")
    val userId: String,
    @Json(name = "exercise")
    val exercise: Exercise,
    @Json(name = "type")
    val type: String,
    @Json(name = "data")
    val data: ArrayList<String>
)