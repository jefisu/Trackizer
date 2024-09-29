package com.jefisu.data.remote.document

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDocument(
    val name: String = "",
    val budget: Float = 0f,
    @SerialName("categoryType") val type: String = "",
    @DocumentId override val id: String = "",
) : FirestoreDocumentSync
