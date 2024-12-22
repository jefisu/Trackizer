package com.jefisu.domain.model

data class Category(
    val name: String,
    val type: CategoryType,
    val budget: Float,
    val usedBudget: Float = 0f,
    override val id: String = "",
) : BaseDomain

enum class CategoryType {
    Transport,
    Entertainment,
    Security,
}