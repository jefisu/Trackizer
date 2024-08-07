package com.jefisu.data.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class SubscriptionDto(
    @DocumentId val id: String = "",
    val userId: String = "",
    val categoryId: String = "",
    val serviceName: String = "",
    val price: Float = 0f,
    val description: String = "",
    val firstPayment: Timestamp = Timestamp.now(),
    val reminder: Boolean = false,
)
