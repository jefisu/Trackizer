package com.jefisu.data.util

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

private val userId = Firebase.auth.currentUser?.uid
private const val USER_ID_KEY = "userId"

fun CollectionReference.fromCurrentUser(): Query = this.whereEqualTo(USER_ID_KEY, userId)
