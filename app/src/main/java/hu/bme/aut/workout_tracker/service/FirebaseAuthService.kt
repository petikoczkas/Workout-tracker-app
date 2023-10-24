package hu.bme.aut.workout_tracker.service

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

object FirebaseAuthService {

    suspend fun signIn(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure()
                }
            }.await()
    }

    fun signOut(firebaseAuth: FirebaseAuth) {
        firebaseAuth.signOut()
    }

    suspend fun registrate(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure()
                }
            }.await()
    }
}