package com.jefisu.data.remote.document

import com.google.firebase.firestore.DocumentId
import com.jefisu.data.remote.FirestoreDocumentSync
import java.time.LocalDate

data class SubscriptionDocument(
    val categoryId: String? = null,
    val cardId: String? = null,
    val serviceName: String = "",
    val price: Float = 0f,
    val description: String = "",
    val firstPaymentEpochDay: Long = LocalDate.now().toEpochDay(),
    val reminder: Boolean = false,
    @DocumentId override val id: String? = null,
    override val offlineId: String = "",
) : FirestoreDocumentSync
