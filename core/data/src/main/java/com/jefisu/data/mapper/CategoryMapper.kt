package com.jefisu.data.mapper

import com.jefisu.data.dto.CategoryDto
import com.jefisu.domain.model.Category
import com.jefisu.domain.model.CategoryType

fun CategoryDto.toCategory() = Category(
    id = id,
    name = name,
    type = CategoryType.valueOf(categoryType),
    budget = budget,
    usedBudget = 0f,
)
