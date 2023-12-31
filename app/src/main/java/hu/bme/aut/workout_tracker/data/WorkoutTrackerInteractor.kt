package hu.bme.aut.workout_tracker.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.service.FirebaseAuthService
import hu.bme.aut.workout_tracker.service.FirebaseStorageService
import hu.bme.aut.workout_tracker.utils.Constants.NAME_PROPERTY
import hu.bme.aut.workout_tracker.utils.Constants.PICTURE_FOLDER
import hu.bme.aut.workout_tracker.utils.Constants.USER_COLLECTION
import kotlinx.coroutines.delay
import javax.inject.Inject

class WorkoutTrackerInteractor @Inject constructor() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference.child(PICTURE_FOLDER)
    private var currentUser = firebaseAuth.currentUser
    private val queryUsers = firebaseFirestore.collection(USER_COLLECTION)

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
        return currentUser != null
    }

    fun getUsers() = FirebaseStorageService.getUsers(
        queryUsersByName = queryUsers.orderBy(NAME_PROPERTY, Query.Direction.ASCENDING),
    )

    suspend fun getCurrentUser() = FirebaseStorageService.getCurrentUser(
        queryUsers = queryUsers,
        userId = currentUser?.uid.toString()
    )

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