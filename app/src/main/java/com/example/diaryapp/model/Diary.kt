package com.example.diaryapp.model

import com.example.diaryapp.util.toRealmInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.Instant

open class Diary : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var ownerId: String = String()
    var mood: String = Mood.Neutral.name
    var title: String = String()
    var description: String = String()
    var images: RealmList<String> = realmListOf()
    var date: RealmInstant = Instant.now().toRealmInstant()
}