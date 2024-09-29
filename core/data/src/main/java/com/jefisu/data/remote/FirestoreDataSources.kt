package com.jefisu.data.remote

import com.jefisu.data.remote.document.CardDocument
import com.jefisu.data.remote.document.CategoryDocument
import com.jefisu.data.remote.document.SubscriptionDocument

data class FirestoreDataSources(
    val subscription: FirestoreDataSource<SubscriptionDocument>,
    val category: FirestoreDataSource<CategoryDocument>,
    val card: FirestoreDataSource<CardDocument>,
)
