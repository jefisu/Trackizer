package com.jefisu.data.remote

import com.jefisu.data.remote.document.CardDocument
import com.jefisu.data.remote.document.CategoryDocument
import com.jefisu.data.remote.document.SubscriptionDocument

data class FirestoreSyncWrapper(
    val subscription: RemoteDataSource<SubscriptionDocument>,
    val category: RemoteDataSource<CategoryDocument>,
    val card: RemoteDataSource<CardDocument>,
)
