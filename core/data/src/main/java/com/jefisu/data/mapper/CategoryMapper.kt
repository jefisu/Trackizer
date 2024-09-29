package com.jefisu.data.mapper

import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.remote.document.CategoryDocument
import com.jefisu.domain.model.Category
import com.jefisu.domain.model.CategoryType
import org.mongodb.kbson.ObjectId

fun Category.toCategoryOffline(): CategoryOffline {
    val category = this
    return CategoryOffline().apply {
        if (category.id.isNotEmpty()) {
            _id = ObjectId(category.id)
        }
        name = category.name
        typeName = category.type.name
        budget = category.budget
    }
}

fun CategoryOffline.toCategory() = Category(
    id = _id.toHexString(),
    name = name,
    type = CategoryType.valueOf(typeName),
    budget = budget,
)

fun CategoryOffline.toCategoryDocument() = CategoryDocument(
    id = cloudId,
    offlineId = _id.toHexString(),
    name = name,
    type = typeName,
    budget = budget,
)

fun CategoryDocument.toCategoryOffline(): CategoryOffline {
    val category = this
    return CategoryOffline().apply {
        _id = ObjectId(category.offlineId)
        cloudId = category.id
        name = category.name
        typeName = category.type
        budget = category.budget
    }
}
