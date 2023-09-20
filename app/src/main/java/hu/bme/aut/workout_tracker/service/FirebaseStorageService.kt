package hu.bme.aut.workout_tracker.service

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import hu.bme.aut.workout_tracker.data.model.User
import hu.bme.aut.workout_tracker.utils.Constants.USER_COLLECTION
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
}