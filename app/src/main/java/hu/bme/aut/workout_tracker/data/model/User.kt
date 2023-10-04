package hu.bme.aut.workout_tracker.data.model

data class User(
    val id: String = "",
    var name: String = "",
    var photo: String = "",
    val workouts: MutableList<String> = mutableListOf(),
    val favoriteWorkouts: MutableList<String> = mutableListOf(),
    val exercises: HashMap<String, MutableList<String>> = HashMap(),
    val charts: HashMap<String, MutableList<Int>> = HashMap()
)
