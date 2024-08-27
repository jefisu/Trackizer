package com.jefisu.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.jefisu.data.dto.CardDto
import com.jefisu.data.mapper.toCard
import com.jefisu.data.mapper.toCardDto
import com.jefisu.domain.model.Card
import com.jefisu.domain.repository.CardRepository
import com.jefisu.domain.util.EmptyResult
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.Result
import com.jefisu.domain.util.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CardRepositoryImpl : CardRepository {

    private val userIdField = "userId"
    private val userId = Firebase.auth.currentUser?.uid

    private val cardCollection = Firebase.firestore.collection("cards")

    override val cards: Flow<List<Card>> = cardCollection
        .whereEqualTo(userIdField, userId)
        .snapshots()
        .map { query ->
            query
                .toObjects<CardDto>()
                .map { it.toCard() }
        }
        .flowOn(Dispatchers.IO)

    override suspend fun saveCard(card: Card): EmptyResult = withContext(Dispatchers.IO) {
        try {
            val cardDto = card.toCardDto().copy(userId = userId!!)
            cardDto.id?.let { id ->
                cardCollection
                    .document(id)
                    .set(cardDto)
                    .await()
                return@withContext Result.Success(Unit)
            }

            cardCollection
                .add(cardDto)
                .await()
            Result.Success(Unit)
        } catch (e: FirebaseFirestoreException) {
            Result.Error(MessageText.Error(UiText.DynamicString(e.message.orEmpty())))
        }
    }
}
