package com.jefisu.ui.util

import com.jefisu.domain.model.Card
import com.jefisu.domain.model.CardFlag
import com.jefisu.domain.model.CardType
import com.jefisu.domain.model.Category
import com.jefisu.domain.model.CategoryType
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.model.SubscriptionService
import java.time.LocalDate

object SampleData {
    const val MONTHLY_BUDGET = 1000f
    val subscriptions = (0..10).map {
        val index = it % 2
        Subscription(
            id = it.toString(),
            description = "Subscription $it",
            service = SubscriptionService.entries[index],
            price = it.toFloat(),
            paymentDate = LocalDate.of(2024, 7, 10).plusDays(it.toLong()),
            reminder = index == 0,
            categoryId = "category$it",
            cardId = "card$it",
        )
    }

    val categories = (0..5).map {
        Category(
            id = it.toString(),
            type = CategoryType.entries.random(),
            budget = 100f * it,
            usedBudget = 10f * it.coerceAtLeast(1),
            name = "Auto & Transport",
        )
    }

    val cards = (0..6).map { index ->
        Card(
            id = index.toString(),
            name = "Card name $index",
            cardHolder = "John Doe $index",
            number = "1234567890123456",
            expirationDate = LocalDate.of(2024, 10, 1),
            flag = CardFlag.MASTERCARD,
            type = CardType.CREDIT,
            cvv = "",
        )
    }.associateWith { card ->
        subscriptions
            .take(card.id.toInt())
            .map { it.service }
    }
}
