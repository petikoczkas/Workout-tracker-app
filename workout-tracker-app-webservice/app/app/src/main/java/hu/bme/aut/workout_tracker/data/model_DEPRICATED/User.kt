package hu.bme.aut.workout_tracker.data.model_DEPRICATED

data class User(
    val id: String = "",
    var name: String = "",
    var photo: String = "",
    val workouts: MutableList<String> = mutableListOf(),
    val favoriteWorkouts: MutableList<String> = mutableListOf(),
    val exercises: HashMap<String, MutableList<String>> = HashMap(),
    val volumeCharts: HashMap<String, MutableList<Int>> = HashMap(),
    val averageOneRepMaxCharts: HashMap<String, MutableList<Double>> = HashMap(),
    val oneRepMaxCharts: HashMap<String, MutableList<Double>> = HashMap()
)
