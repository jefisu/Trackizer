package com.jefisu.data.remote.document

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.PropertyName
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.Serializable

@Serializable
sealed interface FirestoreDocumentSync {
    val id: String

    @get:PropertyName("userId")
    val userId: String? get() = Firebase.auth.currentUser?.uid
}