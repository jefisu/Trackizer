package com.jefisu.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class PendingSyncItem :
    OfflineObject(),
    RealmObject {

    @PrimaryKey
    override var _id: ObjectId = ObjectId()
    override var cloudId: String? = null
    var offlineId: String? = null
    var isSynced: Boolean = false
    var type: String = SyncType.NONE.name
    var action: String = SyncAction.INSERT_OR_UPDATE.name
}

enum class SyncType {
    SUBSCRIPTION,
    CATEGORY,
    CARD,
    NONE,
}

enum class SyncAction {
    INSERT_OR_UPDATE,
    DELETE,
}
