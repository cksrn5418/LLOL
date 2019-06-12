package org.techtown.klab

import android.graphics.Bitmap
import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

data class User(
    var id:String,
    var nickname:String,
    var profile:String,
    var phonenum:String,
    var age:String,
    var flag:String = (-1).toString(),
    var latitude:String,
    var longitude:String
) {
    constructor():this(0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString())
}