package com.jefisu.data.repository

import com.jefisu.data.local.DataSources
import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.mapper.toCategory
import com.jefisu.data.mapper.toCategoryOffline
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.Category
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val dataSources: DataSources,
) : DataRepositoryImpl<Category, CategoryOffline>(
    dataSource = dataSources.category,
    pendingSyncDataSource = dataSources.pendingSync,
    input = Category::toCategoryOffline,
    output = CategoryOffline::toCategory,
)
