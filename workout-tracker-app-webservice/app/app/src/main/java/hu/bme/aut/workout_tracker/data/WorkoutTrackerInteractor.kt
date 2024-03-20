package hu.bme.aut.workout_tracker.data

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.bme.aut.workout_tracker.data.api.WorkoutTrackerAPI
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthLogin
import hu.bme.aut.workout_tracker.data.model.auth.UserAuthRegister
import hu.bme.aut.workout_tracker.service.FirebaseStorageService
import hu.bme.aut.workout_tracker.utils.Constants.currentUserEmail
import hu.bme.aut.workout_tracker.utils.Constants.token
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayOutputStream
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

    suspend fun registrate(
        userAuthRegister: UserAuthRegister,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        workoutTrackerAPI.registrate(userAuthRegister)
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
        userAuthLogin: UserAuthLogin,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {
            val response = workoutTrackerAPI.signIn(userAuthLogin)
            if (response.isNotEmpty()) {
                token = "Bearer $response"
                currentUserEmail = userAuthLogin.email.lowercase()
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

    suspend fun getUsers() = workoutTrackerAPI.getUsers(bearerToken = token)


    suspend fun getCurrentUser(): hu.bme.aut.workout_tracker.data.model.User {
        Log.println(Log.INFO, "currentUserEmail", currentUserEmail)
        Log.println(Log.INFO, "token2", token)
        return workoutTrackerAPI.getCurrentUser(
            bearerToken = token,
            email = currentUserEmail
        )
    }

    suspend fun updateUser(user: User) {
        workoutTrackerAPI.updateUser(
            bearerToken = token,
            user = user
        )
    }

    suspend fun uploadProfilePicture(
        user: User,
        imageBitmap: Bitmap,
        onSuccess: (String) -> Unit,
    ) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        user.photo = Base64.encodeToString(byteArray, Base64.DEFAULT)
        updateUser(user = user)
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