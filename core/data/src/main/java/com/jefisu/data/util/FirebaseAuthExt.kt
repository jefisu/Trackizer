package com.jefisu.data.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun FirebaseAuth.userFlow(): Flow<FirebaseUser?> = callbackFlow {
    val auth = this@userFlow
    val listener = FirebaseAuth.AuthStateListener { state ->
        trySendBlocking(state.currentUser)
    }
    auth.addAuthStateListener(listener)
    awaitClose {
        auth.removeAuthStateListener(listener)
    }
}
