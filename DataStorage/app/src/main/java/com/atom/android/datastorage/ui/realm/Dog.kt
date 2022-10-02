package com.atom.android.datastorage.ui.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Dog : RealmObject() {
    @PrimaryKey
    var id:String = ""
    var name:String = ""
}