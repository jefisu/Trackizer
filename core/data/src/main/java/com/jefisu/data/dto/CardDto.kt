package com.jefisu.data.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class CardDto(
    @DocumentId val id: String? = null,
    val userId: String = "",
    val name: String = "",
    val cardHolder: String = "",
    val number: String = "",
    val expirationDate: Timestamp = Timestamp.now(),
    val cvv: String = "",
    val flag: String = "",
    val type: String = "",
)
