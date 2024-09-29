package com.jefisu.data.remote

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.ktx.Firebase
import com.jefisu.data.StandardDispatcherProvider
import com.jefisu.data.util.safeCallResult
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreDataSource<T : FirestoreDocumentSync>(
    private val collectionName: String,
    private val clazz: Class<T>,
    private val dispatcher: DispatcherProvider = StandardDispatcherProvider,
) : RemoteDataSource<T> {

    private val _collection = Firebase.firestore.collection(collectionName)

    override val data = _collection
        .whereEqualTo("userId", Firebase.auth.currentUser?.uid)
        .snapshots()
        .map { it.toObjects(clazz) }
        .flowOn(dispatcher.io)

    override suspend fun insert(obj: T): Result<String, DataMessage> {
        return safeCallResult(
            dispatcher = dispatcher.io,
            exceptions = mapOf(),
            causedException = "Insert in Firestore - $clazz",
        ) {
            val isUpdating = data.first().firstOrNull { it.id == obj.id } != null
            if (isUpdating) {
                _collection
                    .document(obj.id.toString())
                    .set(obj)
                    .await()
                return@safeCallResult obj.id!!
            }

            return@safeCallResult _collection.add(obj).await().id
        }
    }
}
