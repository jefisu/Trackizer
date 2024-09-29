package com.jefisu.data.local.model

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class PendingSync :
    Offline(),
    RealmObject {

    @PrimaryKey
    override var _id: ObjectId = ObjectId()
    var dataId: String? = null
    var action: SyncAction? = null
}

class SyncAction : EmbeddedRealmObject {

    private var _typeName: String = ""

    @Ignore
    val type: Type by lazy { Type.valueOf(_typeName) }

    private var _dataTypeName: String = ""

    @Ignore
    val dataType: DataType by lazy { DataType.valueOf(_dataTypeName) }

    enum class Type {
        INSERT_OR_UPDATE,
        DELETE,
    }

    enum class DataType {
        SUBSCRIPTION,
        CATEGORY,
        CARD,
    }

    companion object {
        fun set(
            type: Type,
            dataType: DataType,
        ): SyncAction = SyncAction().apply {
            _typeName = type.name
            _dataTypeName = dataType.name
        }
    }
}
