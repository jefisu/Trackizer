package com.jefisu.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.jefisu.data.dto.CategoryDto
import com.jefisu.data.mapper.toCategory
import com.jefisu.data.mapper.toCategoryDto
import com.jefisu.data.util.fromCurrentUser
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.Category
import com.jefisu.domain.repository.CategoryRepository
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.EmptyResult
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.Result
import com.jefisu.domain.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(private val dispatcher: DispatcherProvider) : CategoryRepository {

    private val userId = Firebase.auth.currentUser?.uid
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

    override suspend fun addCategory(category: Category): EmptyResult {
        return withContext(dispatcher.io) {
            try {
                val categoryDto = category
                    .toCategoryDto()
                    .copy(userId = userId)

                categoryDto.id?.let { id ->
                    collection
                        .document(id)
                        .set(categoryDto)
                        .await()
                    return@withContext Result.Success(Unit)
                }

                collection
                    .add(categoryDto)
                    .await()
                Result.Success(Unit)
            } catch (e: FirebaseFirestoreException) {
                Result.Error(
                    MessageText.Error(
                        UiText.DynamicString(e.message.orEmpty()),
                    ),
                )
            }
        }
    }

    override suspend fun deleteCategory(category: Category): Result<DataMessage, DataMessage> =
        withContext(dispatcher.io) {
            try {
                collection.document(category.id).delete().await()
                Result.Success(DataMessage.CATEGORY_DELETED)
            } catch (e: FirebaseFirestoreException) {
                Result.Error(
                    DataMessage.CATEGORY_NOT_DELETED,
                )
            }
        }
}
