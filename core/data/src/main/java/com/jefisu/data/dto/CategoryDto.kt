package com.jefisu.data.dto

import com.google.firebase.firestore.DocumentId

data class CategoryDto(
    @DocumentId val id: String? = null,
    val userId: String? = null,
    val name: String = "",
    val budget: Float = 0f,
    val categoryType: String = "",
)
