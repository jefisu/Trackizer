@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jefisu.data.remote

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.ktx.Firebase
import com.jefisu.data.StandardDispatcherProvider
import com.jefisu.data.remote.document.FirestoreDocumentSync
import com.jefisu.data.util.safeCallResult
import com.jefisu.data.util.userFlow
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirestoreDataSource<T : FirestoreDocumentSync>(
    private val collectionName: String,
    private val clazz: Class<T>,
    private val dispatcher: DispatcherProvider = StandardDispatcherProvider,
) {

    private val _userFlow = Firebase.auth.userFlow()
    private val _collection = Firebase.firestore.collection(collectionName)

    val dataChanges = _userFlow
        .filterNotNull()
        .flatMapMerge { firebaseUser ->
            _collection
                .whereEqualTo("userId", firebaseUser.uid)
                .snapshots()
                .map { snapshot ->
                    snapshot.documentChanges.map { DataChange.from(clazz, it) }
                }
        }

    suspend fun insertOrUpdate(obj: T): Result<Unit, DataMessage> = safeCallResult(
        dispatcher = dispatcher.io,
        causedException = "Insert in Firestore - $clazz",
    ) {
        _collection
            .document(obj.id)
            .set(obj)
            .await()
    }

    suspend fun deleteById(id: String): Result<Unit, DataMessage> = safeCallResult(
        dispatcher = dispatcher.io,
        causedException = "Delete in Firestore - $clazz",
    ) {
        _collection.document(id).delete().await()
    }

    suspend fun deleteAll(): Result<Unit, DataMessage> = safeCallResult(
        dispatcher = dispatcher.io,
        causedException = "Delete all in Firestore - $clazz",
    ) {
        _collection
            .whereEqualTo("userId", _userFlow.firstOrNull()?.uid)
            .get()
            .await()
            .forEach { snapshot ->
                launch {
                    snapshot.reference.delete()
                }
            }
    }
}