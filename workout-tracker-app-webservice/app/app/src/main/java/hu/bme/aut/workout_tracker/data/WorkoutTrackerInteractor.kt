package hu.bme.aut.workout_tracker.data

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.Query
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.bme.aut.workout_tracker.data.api.WorkoutTrackerAPI
import hu.bme.aut.workout_tracker.data.model.UserAuth
import hu.bme.aut.workout_tracker.service.FirebaseStorageService
import hu.bme.aut.workout_tracker.utils.Constants.NAME_PROPERTY
import hu.bme.aut.workout_tracker.utils.Constants.currentUserEmail
import hu.bme.aut.workout_tracker.utils.Constants.token
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject

class WorkoutTrackerInteractor @Inject constructor() {

    private val workoutTrackerAPI: WorkoutTrackerAPI

    /*private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference.child(PICTURE_FOLDER)
    private var currentUser = firebaseAuth.currentUser
    private val queryUsers = firebaseFirestore.collection(USER_COLLECTION)*/

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

    /*suspend fun registrate(
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
    }*/

    suspend fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            val response = workoutTrackerAPI.login(UserAuth(email, password))
            if (response.isNotEmpty()) {
                token = response
                currentUserEmail = email.lowercase()
                onSuccess()
            } else {
                onFailure()
            }
        } catch (e: Exception) {
            Log.println(Log.ERROR, "exception", e.message.toString())
            onFailure()
        }
    }

    fun signOut() {
        token = ""
        currentUserEmail = ""
    }

    fun isLoggedIn(): Boolean {
        return currentUserEmail.isNotEmpty()
    }

    fun getUsers() = FirebaseStorageService.getUsers(
        queryUsersByName = queryUsers.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING),
    )

    suspend fun getCurrentUser(): hu.bme.aut.workout_tracker.data.model.User {
        Log.println(Log.INFO, "currentUserEmail", currentUserEmail)
        Log.println(Log.INFO, "token2", token)
        return workoutTrackerAPI.getCurrentUser(
            bearerToken = "Bearer $token",
            email = currentUserEmail
        )
    }

    /*suspend fun getCurrentUser(): User {
        return FirebaseStorageService.getCurrentUser(
            queryUsers = queryUsers,
            userId = currentUser?.uid.toString()
        )
    }*/

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