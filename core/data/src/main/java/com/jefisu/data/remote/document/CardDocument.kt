package com.jefisu.data.remote.document

import com.google.firebase.firestore.DocumentId
import java.time.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CardDocument(
    val name: String = "",
    val cardHolder: String = "",
    val number: String = "",
    val expirationEpochDay: Long = LocalDate.now().toEpochDay(),
    val cvv: String = "",
    val flagName: String = "",
    val typeName: String = "",
    @DocumentId override val id: String = "",
) : FirestoreDocumentSync
