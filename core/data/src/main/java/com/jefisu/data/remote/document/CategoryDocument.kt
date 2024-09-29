package com.jefisu.data.remote.document

import com.google.firebase.firestore.DocumentId
import com.jefisu.data.remote.FirestoreDocumentSync

data class CategoryDocument(
    val name: String = "",
    val budget: Float = 0f,
    val type: String = "",
    @DocumentId override val id: String? = null,
    override val offlineId: String = "",
) : FirestoreDocumentSync
