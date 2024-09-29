package com.jefisu.data.remote.document

import com.google.firebase.firestore.DocumentId
import com.jefisu.data.remote.FirestoreDocumentSync
import java.time.LocalDate

data class CardDocument(
    val name: String = "",
    val cardHolder: String = "",
    val number: String = "",
    val expirationEpochDay: Long = LocalDate.now().toEpochDay(),
    val cvv: String = "",
    val flagName: String = "",
    val typeName: String = "",
    @DocumentId override val id: String? = null,
    override val offlineId: String = "",
) : FirestoreDocumentSync
