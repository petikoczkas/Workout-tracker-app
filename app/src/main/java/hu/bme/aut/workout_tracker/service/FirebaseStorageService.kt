package hu.bme.aut.workout_tracker.service

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import hu.bme.aut.workout_tracker.data.model.Exercise
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.data.model.Workout
import hu.bme.aut.workout_tracker.utils.Constants.EXERCISE_COLLECTION
import hu.bme.aut.workout_tracker.utils.Constants.NAME_PROPERTY
import hu.bme.aut.workout_tracker.utils.Constants.USER_COLLECTION
import hu.bme.aut.workout_tracker.utils.Constants.WORKOUT_COLLECTION
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object FirebaseStorageService {

    suspend fun createUser(
        firebaseFirestore: FirebaseFirestore,
        user: User,
        onSuccess: () -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        firebaseFirestore.collection(USER_COLLECTION).document(user.id).set(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(it.exception)
                }
            }.await()
    }

    suspend fun getCurrentUser(
        queryUsers: Query,
        userId: String
    ): User {
        var currentUser = User()
        queryUsers.get().await().map { document ->
            if (document.id == userId) currentUser = document.toObject(User::class.java)
        }
        return currentUser
    }

    suspend fun updateUser(
        firebaseFirestore: FirebaseFirestore,
        user: User,
    ) {
        firebaseFirestore.collection(USER_COLLECTION).document(user.id).set(user).await()
    }

    suspend fun uploadProfilePicture(
        firebaseStorage: StorageReference,
        userId: String,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
    ) {
        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg")
            .build()

        firebaseStorage.child(userId).putFile(imageUri, metadata)
            .addOnSuccessListener { taskSnapshot ->
                val result = taskSnapshot.metadata?.reference?.downloadUrl
                result?.addOnSuccessListener {
                    onSuccess(it.toString())
                }
            }.await()
    }

    fun getExercises(
        firebaseFirestore: FirebaseFirestore,
    ): Flow<List<Exercise>> {
        return callbackFlow {
            val listener = firebaseFirestore.collection(EXERCISE_COLLECTION)
                .orderBy(NAME_PROPERTY, Query.Direction.ASCENDING)
                .addSnapshotListener { value, e ->
                    e?.let {
                        return@addSnapshotListener
                    }
                    value?.let {
                        val tmp = mutableListOf<Exercise>()
                        for (d in it.documents) {
                            d.toObject(Exercise::class.java)
                                ?.let { doc -> tmp.add(doc) }
                        }
                        trySend(tmp.toList())
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    fun getUserWorkouts(
        firebaseFirestore: FirebaseFirestore,
        user: User
    ): Flow<List<Workout>> {
        return callbackFlow {
            val listener = firebaseFirestore.collection(WORKOUT_COLLECTION)
                .addSnapshotListener { value, e ->
                    e?.let {
                        return@addSnapshotListener
                    }
                    value?.let {
                        val tmp = mutableListOf<Workout>()
                        for (d in it.documents) {
                            for (wId in user.workouts) {
                                if (wId == d.id) {
                                    d.toObject(Workout::class.java)?.let { doc ->
                                        tmp.add(doc)
                                    }
                                }
                            }
                        }
                        trySend(tmp.toList())
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    fun getUserFavoriteWorkouts(
        firebaseFirestore: FirebaseFirestore,
        user: User
    ): Flow<List<Workout>> {
        return callbackFlow {
            val listener = firebaseFirestore.collection(WORKOUT_COLLECTION)
                .addSnapshotListener { value, e ->
                    e?.let {
                        return@addSnapshotListener
                    }
                    value?.let {
                        val tmp = mutableListOf<Workout>()
                        for (d in it.documents) {
                            for (wId in user.favoriteWorkouts) {
                                if (wId == d.id) {
                                    d.toObject(Workout::class.java)?.let { doc ->
                                        tmp.add(doc)
                                    }
                                }
                            }
                        }
                        trySend(tmp.toList())
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    fun getWorkoutExercises(
        firebaseFirestore: FirebaseFirestore,
        workout: Workout
    ): Flow<List<Exercise>> {
        return callbackFlow {
            val listener = firebaseFirestore.collection(EXERCISE_COLLECTION)
                .addSnapshotListener { value, e ->
                    e?.let {
                        return@addSnapshotListener
                    }
                    value?.let {
                        val tmp = mutableListOf<Exercise>()
                        for (d in it.documents) {
                            for (eId in workout.exercises) {
                                if (eId == d.id) {
                                    d.toObject(Exercise::class.java)?.let { doc ->
                                        tmp.add(doc)
                                    }
                                }
                            }
                        }
                        trySend(tmp.toList())
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    suspend fun getWorkout(
        firebaseFirestore: FirebaseFirestore,
        workoutId: String
    ): Workout {
        var wokrout = Workout()
        firebaseFirestore.collection(WORKOUT_COLLECTION).get().await().map { document ->
            if (document.id == workoutId) wokrout = document.toObject(Workout::class.java)
        }
        return wokrout
    }

    suspend fun createExercise(
        firebaseFirestore: FirebaseFirestore,
        exercise: Exercise
    ) {
        firebaseFirestore.collection(EXERCISE_COLLECTION).document(exercise.id).set(exercise)
            .await()
    }

    suspend fun updateWorkout(
        firebaseFirestore: FirebaseFirestore,
        workout: Workout
    ) {
        if (workout.exercises.isEmpty()) firebaseFirestore.collection(WORKOUT_COLLECTION)
            .document(workout.id).delete().await()
        else firebaseFirestore.collection(WORKOUT_COLLECTION).document(workout.id).set(workout)
            .await()
    }
}