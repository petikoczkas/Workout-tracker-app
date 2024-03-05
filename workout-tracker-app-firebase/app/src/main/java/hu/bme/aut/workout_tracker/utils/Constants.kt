package hu.bme.aut.workout_tracker.utils

import hu.bme.aut.workout_tracker.data.model.Exercise

object Constants {
    const val USER_COLLECTION = "user"
    const val EXERCISE_COLLECTION = "exercise"
    const val WORKOUT_COLLECTION = "workout"
    const val PICTURE_FOLDER = "profilePicture"
    const val NAME_PROPERTY = "name"

    val BODY_PARTS = listOf(
        "Chest",
        "Back",
        "Shoulders",
        "Triceps",
        "Biceps",
        "Legs",
        "Core",
        "Forearms"
    )
    val chartsList = listOf(
        "Volume Chart",
        "Average 1RM Chart",
        "1RM Chart",
    )
    val addedExercises = mutableListOf<Exercise>()
}
