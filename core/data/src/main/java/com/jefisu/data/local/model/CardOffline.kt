package com.jefisu.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.LocalDate
import org.mongodb.kbson.ObjectId

class CardOffline :
    Offline(),
    RealmObject {

    @PrimaryKey
    override var _id: ObjectId = ObjectId()
    var name: String = ""
    var cardHolder: String = ""
    var number: String = ""
    var expirationEpochDay: Long = LocalDate.now().toEpochDay()
    var cvv: String = ""
    var flagName: String = ""
    var typeName: String = ""
}
