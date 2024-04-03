package hu.bme.aut.workout_tracker.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Arrays

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "email")
    val email: String,
    @Json(name = "firstName")
    var firstName: String,
    @Json(name = "lastName")
    var lastName: String,
    @Json(name = "photo")
    var photo: ByteArray
) {
    constructor() : this("", "", "", byteArrayOf())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (email != other.email) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (!photo.contentEquals(other.photo)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + Arrays.hashCode(photo)
        return result
    }
}