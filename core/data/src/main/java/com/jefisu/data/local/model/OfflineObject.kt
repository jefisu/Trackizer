package com.jefisu.data.local.model

import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.ObjectId

abstract class OfflineObject : RealmObject {
    abstract var _id: ObjectId
    abstract var cloudId: String?
}
