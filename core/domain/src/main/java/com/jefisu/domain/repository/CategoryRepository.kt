package com.jefisu.domain.repository

import com.jefisu.domain.model.Category
import com.jefisu.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    val categories: Flow<List<Category>>
    suspend fun getCategoryById(id: String): Category?
    suspend fun addCategory(category: Category): EmptyResult
}
