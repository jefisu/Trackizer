package com.jefisu.data.local.model

import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.ObjectId

sealed class Offline : RealmObject {
    abstract var _id: ObjectId
}
