package com.jefisu.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class CategoryOffline :
    OfflineObject(),
    RealmObject {

    @PrimaryKey
    override var _id: ObjectId = ObjectId()
    override var cloudId: String? = null
    var name: String = ""
    var typeName: String = ""
    var budget: Float = 0f
}
