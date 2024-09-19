package com.jefisu.domain.model

data class Category(
    val name: String,
    val type: CategoryType,
    val budget: Float,
    val id: String = "",
    val usedBudget: Float = 0f,
)

enum class CategoryType {
    Transport,
    Entertainment,
    Security,
}
