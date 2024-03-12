package hu.bme.aut.workout_tracker.data

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.bme.aut.workout_tracker.data.api.WorkoutTrackerAPI
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.data.model2.UserAuth
import hu.bme.aut.workout_tracker.service.FirebaseAuthService
import hu.bme.aut.workout_tracker.service.FirebaseStorageService
import hu.bme.aut.workout_tracker.utils.Constants.NAME_PROPERTY
import hu.bme.aut.workout_tracker.utils.Constants.PICTURE_FOLDER
import hu.bme.aut.workout_tracker.utils.Constants.USER_COLLECTION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject

class WorkoutTrackerInteractor @Inject constructor() {

    private val workoutTrackerAPI: WorkoutTrackerAPI

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference.child(PICTURE_FOLDER)
    private var currentUser = firebaseAuth.currentUser
    private val queryUsers = firebaseFirestore.collection(USER_COLLECTION)

    init {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.106:8080/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        workoutTrackerAPI = retrofit.create(WorkoutTrackerAPI::class.java)
    }

    companion object {
        var token = ""
    }

    suspend fun login(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = workoutTrackerAPI.login(UserAuth(email, password))
            if (response.isSuccessful) {
                launch(Dispatchers.Main) {
                    response.body()?.let {
                        token = it
                        //Log.println(Log.INFO, "tokeeeeeen", token)
                    }
                }
            }
        }
    }

    suspend fun getCurrentUser(email: String): hu.bme.aut.workout_tracker.data.model2.User {
        //Log.println(Log.INFO, "tokeeeeeen2", token)
        return workoutTrackerAPI.getCurrentUser(
            bearerToken = "Bearer $token",
            email = email
        )
    }


    suspend fun registrate(
        email: String,
        password: String,
        user: User,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        var userWithID = user

        FirebaseAuthService.registrate(
            firebaseAuth = firebaseAuth,
            email = email,
            password = password,
            onSuccess = {
                onSuccess()
                currentUser = firebaseAuth.currentUser
                userWithID = user.copy(id = currentUser?.uid.toString())
            },
            onFailure = {
                onFailure()
            }
        )
        delay(5)
        FirebaseStorageService.createUser(
            firebaseFirestore = firebaseFirestore,
            user = userWithID,
            onSuccess = {
                onSuccess()
            },
            onFailure = {
                onFailure()
            }
        )
    }

    suspend fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        login("a@a.com", "123")
        FirebaseAuthService.signIn(
            firebaseAuth = firebaseAuth,
            email = email,
            password = password,
            onSuccess = {
                onSuccess()
                currentUser = firebaseAuth.currentUser
            },
            onFailure = {
                onFailure()
            }
        )
    }

    fun signOut() {
        FirebaseAuthService.signOut(firebaseAuth = firebaseAuth)
    }

    fun isLoggedIn(): Boolean {
        return false//currentUser != null
    }

    fun getUsers() = FirebaseStorageService.getUsers(
        queryUsersByName = queryUsers.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING),
    )

    suspend fun getCurrentUser(): User {
        Log.println(Log.INFO, "nanana", getCurrentUser("a@a.com").email)
        return FirebaseStorageService.getCurrentUser(
            queryUsers = queryUsers,
            userId = currentUser?.uid.toString()
        )
    }

    suspend fun updateUser(user: User) {
        FirebaseStorageService.updateUser(
            firebaseFirestore = firebaseFirestore,
            user = user,
        )
    }

    suspend fun uploadProfilePicture(
        userId: String,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
    ) {
        FirebaseStorageService.uploadProfilePicture(
            firebaseStorage = firebaseStorage,
            userId = userId,
            imageUri = imageUri,
            onSuccess = onSuccess
        )
    }

    fun getExercises() = FirebaseStorageService.getExercises(
        firebaseFirestore = firebaseFirestore
    )

    fun getStandingsExercises() = FirebaseStorageService.getStandingsExercises(
        firebaseFirestore = firebaseFirestore
    )

    fun getUserWorkouts(user: User) =
        FirebaseStorageService.getUserWorkouts(
            firebaseFirestore = firebaseFirestore,
            user = user
        )

    fun getUserFavoriteWorkouts(user: User) =
        FirebaseStorageService.getUserFavoriteWorkouts(
            firebaseFirestore = firebaseFirestore,
            user = user
        )

    fun getWorkoutExercises(workout: Workout) =
        FirebaseStorageService.getWorkoutExercises(
            firebaseFirestore = firebaseFirestore,
            workout = workout
        )

    suspend fun getWorkout(workoutId: String) =
        FirebaseStorageService.getWorkout(
            firebaseFirestore = firebaseFirestore,
            workoutId = workoutId
        )

    suspend fun createExercise(exercise: Exercise) {
        FirebaseStorageService.createExercise(
            firebaseFirestore = firebaseFirestore,
            exercise = exercise
        )
    }

    suspend fun updateWorkout(workout: Workout) {
        FirebaseStorageService.updateWorkout(
            firebaseFirestore = firebaseFirestore,
            workout = workout
        )
    }
}