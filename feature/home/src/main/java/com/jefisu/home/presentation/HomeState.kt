package com.jefisu.home.presentation

import com.jefisu.domain.model.Subscription

data class HomeState(
    val monthlyBudget: Float = 0f,
    val subscriptions: List<Subscription> = emptyList(),
)
