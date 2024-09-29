package com.jefisu.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.LocalDate
import org.mongodb.kbson.ObjectId

class SubscriptionOffline :
    OfflineObject(),
    RealmObject {
    @PrimaryKey
    override var _id: ObjectId = ObjectId()
    override var cloudId: String? = null
    var serviceName: String = ""
    var description: String = ""
    var price: Float = 0f
    var firstPaymentEpochDay: Long = LocalDate.now().toEpochDay()
    var reminder: Boolean = false
    var categoryId: String? = null
    var cardId: String? = null
}
