package hu.bme.aut.workout_tracker.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import hu.bme.aut.workout_tracker.data.model.Exercise

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "savedData")

object AppData {
    val TOKEN = stringPreferencesKey("token")
    val USER_EMAIL = stringPreferencesKey("userEmail")

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

    var currentUserEmail = ""

    var token = ""
}
