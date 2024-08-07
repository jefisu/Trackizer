package com.jefisu.domain.model

data class Category(
    val id: String,
    val name: String,
    val type: CategoryType,
    val budget: Float,
    val usedBudget: Float,
)
