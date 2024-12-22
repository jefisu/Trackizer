package com.jefisu.data.remote.document

import com.google.firebase.firestore.DocumentId
import java.time.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionDocument(
    val categoryId: String? = null,
    val cardId: String? = null,
    val serviceName: String = "",
    val price: Float = 0f,
    val description: String = "",
    val firstPaymentEpochDay: Long = LocalDate.now().toEpochDay(),
    val reminder: Boolean = false,
    @DocumentId override val id: String = "",
) : FirestoreDocumentSync