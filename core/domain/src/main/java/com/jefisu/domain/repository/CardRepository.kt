package com.jefisu.domain.repository

import com.jefisu.domain.model.Card
import com.jefisu.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    val cards: Flow<List<Card>>

    suspend fun saveCard(card: Card): EmptyResult
    suspend fun getCardById(id: String): Card?
}
