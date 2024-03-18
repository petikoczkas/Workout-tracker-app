package hu.bme.aut.workout_tracker.data.model_DEPRICATED

import java.util.UUID

data class Exercise(
    val id: String = UUID.randomUUID().toString(),
    val category: String = "",
    val name: String = ""
)