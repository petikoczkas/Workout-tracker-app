package hu.bme.aut.workout_tracker.data.model_DEPRICATED

import java.util.UUID

data class Workout(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val user: String = "",
    val exercises: MutableList<String> = mutableListOf()
)