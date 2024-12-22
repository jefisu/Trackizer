package com.jefisu.data.local

data class DataSources(
    val subscription: SubscriptionDataSource,
    val category: CategoryDataSource,
    val card: CardDataSource,
    val pendingSync: PendingSyncDataSource,
)