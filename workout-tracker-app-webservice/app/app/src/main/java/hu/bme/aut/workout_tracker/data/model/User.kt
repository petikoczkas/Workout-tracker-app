package hu.bme.aut.workout_tracker.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "email")
    val email: String,
    @Json(name = "firstName")
    var firstName: String,
    @Json(name = "lastName")
    var lastName: String,
    @Json(name = "photo")
    var photo: String
) {
    constructor() : this("", "", "", "")
}