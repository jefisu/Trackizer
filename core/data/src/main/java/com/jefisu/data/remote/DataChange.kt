package com.jefisu.data.remote

import com.google.firebase.firestore.DocumentChange
import com.jefisu.data.remote.document.FirestoreDocumentSync
import kotlinx.serialization.Serializable

@Serializable
sealed class DataChange(val document: FirestoreDocumentSync) {

    @Serializable
    class Added(private val doc: FirestoreDocumentSync) : DataChange(doc)

    @Serializable
    class Modified(private val doc: FirestoreDocumentSync) : DataChange(doc)

    @Serializable
    class Removed(private val doc: FirestoreDocumentSync) : DataChange(doc)

    inline fun onAdded(action: (FirestoreDocumentSync) -> Unit): DataChange {
        if (this is Added) action(document)
        return this
    }

    inline fun onModified(action: (FirestoreDocumentSync) -> Unit): DataChange {
        if (this is Modified) action(document)
        return this
    }

    inline fun onRemoved(action: (FirestoreDocumentSync) -> Unit): DataChange {
        if (this is Removed) action(document)
        return this
    }

    companion object {
        fun from(
            clazz: Class<out FirestoreDocumentSync>,
            documentChange: DocumentChange,
        ): DataChange {
            val document = documentChange.document.toObject(clazz)
            return when (documentChange.type) {
                DocumentChange.Type.ADDED -> Added(document)
                DocumentChange.Type.MODIFIED -> Modified(document)
                DocumentChange.Type.REMOVED -> Removed(document)
            }
        }
    }
}