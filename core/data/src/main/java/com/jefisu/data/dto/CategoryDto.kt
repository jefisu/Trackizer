package com.jefisu.data.dto

import com.google.firebase.firestore.DocumentId

data class CategoryDto(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val budget: Float = 0f,
    val categoryType: String = "",
)
