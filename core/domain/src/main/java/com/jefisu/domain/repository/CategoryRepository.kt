package com.jefisu.domain.repository

import com.jefisu.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    val categories: Flow<List<Category>>
    suspend fun getCategoryById(id: String): Category?
}
