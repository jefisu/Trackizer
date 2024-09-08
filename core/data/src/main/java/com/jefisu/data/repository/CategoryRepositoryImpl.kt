package com.jefisu.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.jefisu.data.dto.CategoryDto
import com.jefisu.data.mapper.toCategory
import com.jefisu.data.util.fromCurrentUser
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.Category
import com.jefisu.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(private val dispatcher: DispatcherProvider) : CategoryRepository {

    private val collection = Firebase.firestore.collection("categories")

    override val categories: Flow<List<Category>> = collection
        .fromCurrentUser()
        .snapshots()
        .map { query ->
            query
                .toObjects<CategoryDto>()
                .map { it.toCategory() }
        }
        .flowOn(dispatcher.io)

    override suspend fun getCategoryById(id: String): Category? = withContext(dispatcher.io) {
        categories.first().firstOrNull { it.id == id }
    }
}
